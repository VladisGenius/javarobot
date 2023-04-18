package Controllers;

import models.RobotModel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    private final Timer m_timer = initTimer();
    private RobotModel m_model;
    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    public Controller(RobotModel model){
        m_model=model;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateModel();
            }
        }, 0, 10);
    }
    public void setTargetPos(Point p){
        m_model.setTargetPosition(p);
    }
    public void updateModel(){
        m_model.updatePos();
    }
}
