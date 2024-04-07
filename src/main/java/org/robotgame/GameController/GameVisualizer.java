package org.robotgame.GameController;

import org.robotgame.GameController.Entities.Robot;
import org.robotgame.GameController.Entities.Target;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameVisualizer extends JPanel {
    private final Timer m_timer = initTimer();
    private final Robot robot;
    private final Target target;
    private final CameraMap cameraMap;
    private Image backgroundImage;

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public GameVisualizer(int width, int height) {
        int startRobotX = 100;
        int startRobotY = 100;
        robot = new Robot(startRobotX, startRobotY, 0);
        target = new Target(startRobotX, startRobotY);
        cameraMap = new CameraMap(startRobotX, startRobotY, width, height,2000, 2000);


        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/map/map.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 7);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 5);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getX(), e.getY());
                repaint();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updateScreenSize();
                repaint();
            }
        });

        setDoubleBuffered(true);
        setFocusable(true);
    }

    public void setTargetPosition(int x, int y) {
        x = (int) (cameraMap.getX() + x);
        y = (int) (cameraMap.getY() + y);

        if (this.getWidth() != 0 && this.getHeight() != 0) {
            x = (int) applyLimits(x, 0, cameraMap.getMapSizeX());
            y = (int) applyLimits(y, 0, cameraMap.getMapSizeY());
        }
        if (distance(x, y, target.getPositionX(), target.getPositionY()) < 5)
            return;

        target.setPositionX(x);
        target.setPositionY(y);
    }



    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public void onModelUpdateEvent() {
        double distance = distance(target.getPositionX(), target.getPositionY(), robot.getPositionX(), robot.getPositionY());
        if (distance < 1) {
            return;
        }

        double angleToTarget = angleTo(robot.getPositionX(), robot.getPositionY(), target.getPositionX(), target.getPositionY());

        double angleDifference;
        if (Math.abs(angleToTarget - robot.getDirection()) > Math.exp(-17)) {
            angleDifference = angleToTarget - robot.getDirection();
        } else {
            angleDifference = Math.exp(-16);
        }

        if (Math.abs(angleDifference) > Math.PI) {
            angleDifference -= Math.signum(angleDifference) * 2 * Math.PI;
        }
        double angularVelocity = robot.getMaxAngularVelocity() * angleDifference;

        moveRobot(angularVelocity, 5);

    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private void moveRobot(double angularVelocity, double duration) {
        double velocity = robot.getMaxVelocity();
        double newX = robot.getPositionX() + velocity / angularVelocity *
                (Math.sin(robot.getDirection() + angularVelocity * duration) -
                        Math.sin(robot.getDirection()));
        if (!Double.isFinite(newX)) {
            newX = robot.getPositionX() + velocity * duration * Math.cos(robot.getDirection());
        }
        double newY = robot.getPositionY() - velocity / angularVelocity *
                (Math.cos(robot.getDirection() + angularVelocity * duration) -
                        Math.cos(robot.getDirection()));
        if (!Double.isFinite(newY)) {
            newY = robot.getPositionY() + velocity * duration * Math.sin(robot.getDirection());
        }
        robot.setPositionX(applyLimits(newX, 0, cameraMap.getMapSizeX()));
        robot.setPositionY(applyLimits(newY, 0, cameraMap.getMapSizeY()));

        double newDirection = asNormalizedRadians(robot.getDirection() + angularVelocity * duration);
        robot.setDirection(newDirection);

        cameraMap.updateCameraMapPosition(robot.getPositionX(), robot.getPositionY());
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Получить текущие координаты камеры
        double cameraX = cameraMap.getX();
        double cameraY = cameraMap.getY();

        drawBackgroundImage(g2d, cameraX, cameraY);
        drawRobot(g2d, robot.getPositionX() - cameraX, robot.getPositionY() - cameraY, robot.getDirection());
        drawTarget(g2d, target.getPositionX() - cameraX, target.getPositionY() - cameraY);
    }



    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, double x, double y, double direction) {

        int robotCenterX = round(x);
        int robotCenterY = round(y);

        AffineTransform originalTransform = g.getTransform();

        g.rotate(direction, robotCenterX, robotCenterY);

        g.setColor(Color.CYAN);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);

        g.setTransform(originalTransform);
    }

    private void drawTarget(Graphics2D g, double x, double y) {
        int targetCenterX = round(x);
        int targetCenterY = round(y);

        g.setColor(Color.RED);
        fillOval(g, targetCenterX, targetCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, targetCenterX, targetCenterY, 5, 5);
    }

    private void drawBackgroundImage(Graphics2D g, double cameraX, double cameraY) {
        int destX = 0; // Координата X верхнего левого угла области
        int destY = 0; // Координата Y верхнего левого угла области
        int destWidth = getWidth(); // Ширина области
        int destHeight = getHeight(); // Высота области

        int srcX = (int) cameraX; // Начальная координата X на изображении
        int srcY = (int) cameraY; // Начальная координата Y на изображении
        int srcWidth = getWidth(); // Ширина области на изображении
        int srcHeight = getHeight(); // Высота области на изображении

        g.drawImage(backgroundImage, destX, destY, destWidth, destHeight, srcX, srcY, srcX + srcWidth, srcY + srcHeight, this);
    }

    public void updateScreenSize(){
        cameraMap.setScreenSize(getWidth(), getHeight());
    }

    public Point getTargetPoint() {
        return new Point(target.getPositionX(), target.getPositionY());
    }
    public Point getRobotPoint() {
        return new Point((int)robot.getPositionX(), (int)robot.getPositionY());
    }
}
