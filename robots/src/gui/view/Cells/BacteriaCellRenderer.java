package gui.view.Cells;

import gui.model.Cells.BacteriaCellEntity;
import gui.model.Cells.CellEntity;
import gui.model.Directions;

import java.awt.*;

public class BacteriaCellRenderer extends BasicCellRenderer {
    @Override
    public void render(CellEntity entity, Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;

        drawCell(g, (BacteriaCellEntity) entity);
    }
    private void drawCell(Graphics2D g, BacteriaCellEntity entity) {
        Point coords = entity.getCoords();
        int cellSize = entity.getCellSize();
        int gridStroke = entity.getGridStroke();
        int eyeSize = entity.getEyeSize();
        String health = String.valueOf(entity.getHealth());
        Directions faceDirection = entity.getCellDirection();

        int cell_x = cellSize * coords.x + (coords.x + 1) * gridStroke;
        int cell_y = cellSize * coords.y + (coords.y + 1) * gridStroke;

        Font font = new Font("Arial", Font.BOLD, 16);
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics();

        g.setColor(entity.getColor());
        g.fillRect(cell_x, cell_y, cellSize, cellSize);
        g.setColor(Color.BLACK);
        g.drawString(health,
                cell_x + (cellSize - fontMetrics.stringWidth(health)) / 2,
                cell_y + (cellSize - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent()
        );

        Point[] eyesCoords = getEyesCoords(faceDirection, cellSize, eyeSize);
        Point cellCenterCoords = new Point(cell_x+cellSize/2, cell_y+cellSize/2);
        for(int i = 0; i < 2; i++){
            g.setColor(Color.black);
            g.drawOval(cellCenterCoords.x+eyesCoords[i].x, cellCenterCoords.y+eyesCoords[i].y, eyeSize, eyeSize);
            g.setColor(Color.cyan);
            g.fillOval(cellCenterCoords.x+eyesCoords[i].x, cellCenterCoords.y+eyesCoords[i].y, eyeSize, eyeSize);

            g.setColor(Color.black);
            g.drawOval(cellCenterCoords.x+eyesCoords[i].x, cellCenterCoords.y+eyesCoords[i].y, eyeSize/2, eyeSize/2);
            g.fillOval(cellCenterCoords.x+eyesCoords[i].x, cellCenterCoords.y+eyesCoords[i].y, eyeSize/2, eyeSize/2);
        }
    }

    /**
     * Возвращает левые верхние координаты точек глаз относительно центра клетки.
     * @return Point[2]
     */
    public Point[] getEyesCoords(Directions faceDirection, int cellSize, int eyeSize){
        Point[] eyesCoords = new Point[2];
        switch (faceDirection) {
            case NORTH:
            case NORTH_EAST:
            case NORTH_WEST:
                eyesCoords[0] = new Point(-cellSize/4-eyeSize/2, -cellSize/2);
                eyesCoords[1] = new Point(cellSize/4-eyeSize/2, -cellSize/2);
                break;
            case EAST:
                eyesCoords[0] = new Point(cellSize/2-eyeSize, -cellSize/4 - eyeSize/2);
                eyesCoords[1] = new Point(cellSize/2-eyeSize, cellSize/4 - eyeSize/2);
                break;
            case SOUTH:
            case SOUTH_EAST:
            case SOUTH_WEST:
                eyesCoords[0] = new Point(-cellSize/4-eyeSize/2, cellSize/2-eyeSize);
                eyesCoords[1] = new Point(cellSize/4-eyeSize/2, cellSize/2-eyeSize);
                break;
            case WEST:
                eyesCoords[0] = new Point(-cellSize/2, -cellSize/4 - eyeSize/2);
                eyesCoords[1] = new Point(-cellSize/2, cellSize/4 - eyeSize/2);
                break;
        }
        return eyesCoords;
    }
}
