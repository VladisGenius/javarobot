package test;

import gui.*;
import org.junit.Test;
import org.junit.Assert;


public class GameFieldTest{
    private Snake snake;
    private Target target;
    private Field field;

    @Test
    public void snackEatTargetTest() {
        target = new Target(100.4, 100);
        snake = new Snake(100, 100);
        field = new Field(snake, target, null, null);
        snake.onModelUpdateEvent(Directions.RIGHT);
        snake.onModelUpdateEvent(Directions.RIGHT);
        Assert.assertTrue(field.isHitTarget());
    }

    @Test
    public void moveSnackRight()
    {
        snake = new Snake(100, 100);
        snake.onModelUpdateEvent(Directions.RIGHT);
        Assert.assertEquals(snake.Head.X_Position, 100 + Config.SPEED, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100, 0.002);
    }

    @Test
    public void moveSnackDown()
    {
        snake = new Snake(100, 100);
        snake.onModelUpdateEvent(Directions.DOWN);
        Assert.assertEquals(snake.Head.X_Position, 100, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100 + Config.SPEED, 0.002);
    }

    @Test
    public void moveSnackUp()
    {
        snake = new Snake(100, 100);
        snake.onModelUpdateEvent(Directions.UP);
        Assert.assertEquals(snake.Head.X_Position, 100, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100 - Config.SPEED, 0.002);
    }

    @Test
    public void moveSnackLeft()
    {
        snake = new Snake(100, 100);
        snake.onModelUpdateEvent(Directions.LEFT);
        Assert.assertEquals(snake.Head.X_Position, 100 - Config.SPEED, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100, 0.002);
    }

    @Test
    public void moveSnackLongSnack()
    {
        snake = new Snake(100, 100);
        snake.onModelUpdateEvent(Directions.LEFT);
        snake.onModelUpdateEvent(Directions.UP);
        snake.onModelUpdateEvent(Directions.RIGHT);
        snake.onModelUpdateEvent(Directions.DOWN);
        Assert.assertEquals(snake.Head.X_Position, 100, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100, 0.002);
    }

    @Test
    public void snackGrowUpTest() {
        target = new Target(99.8, 100);
        snake = new Snake(100, 100);
        field = new Field(snake, target, null, null);
        field.onModelUpdateEvent(Directions.LEFT);
        Assert.assertEquals(snake.getSnakeBlocks().size(), 2);
    }

    @Test
    public void moveSnackTailTest() {
        target = new Target(100, 99.8);
        snake = new Snake(100, 100);
        field = new Field(snake, target, null, null);
        field.onModelUpdateEvent(Directions.UP);
        Assert.assertEquals(snake.getSnakeBlocks().size(), 1);
        Assert.assertEquals(snake.Tail.Y_Position, 100 - Config.SPEED, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100 - Config.SPEED, 0.002);
        field.onModelUpdateEvent(Directions.UP);
        Assert.assertEquals(snake.Tail.Y_Position, 100 - 2 * Config.SPEED, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100 - 2 * Config.SPEED, 0.002);
    }

    @Test
    public void moveSnackTest() {
        target = new Target(200.2, 100);
        snake = new Snake(200, 100);
        field = new Field(snake, target, null, null);
        field.onModelUpdateEvent(Directions.RIGHT);
        field.onModelUpdateEvent(Directions.RIGHT);
        field.onModelUpdateEvent(Directions.UP);
        target.setTargetPosition(200.4, 99.6);
        field.onModelUpdateEvent(Directions.UP);
        Assert.assertEquals(snake.getSnakeBlocks().size(), 3);
        Assert.assertEquals(snake.Tail.Y_Position, 100 - 2 * Config.SPEED, 0.001);
        Assert.assertEquals(snake.Head.Y_Position, 100 - 2 * Config.SPEED, 0.002);
        field.onModelUpdateEvent(Directions.UP);
        Assert.assertEquals(snake.Tail.Y_Position, 100 - 2 * Config.SPEED, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100 - 3 * Config.SPEED, 0.002);
        field.onModelUpdateEvent(Directions.UP);
        Assert.assertEquals(snake.Tail.Y_Position, 100 - 2 * Config.SPEED, 0.002);
        Assert.assertEquals(snake.Head.Y_Position, 100 - 4 * Config.SPEED, 0.001);
    }
}
