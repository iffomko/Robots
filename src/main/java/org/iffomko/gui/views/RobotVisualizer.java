package org.iffomko.gui.views;

import org.iffomko.gui.models.Robot;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * <p>Отрисовывает модель робота <code>Robot</code></p>
 */
public class RobotVisualizer extends Visualizer {
    private final Robot robot;

    /**
     * Инициализирует поле, содержащее модель робота
     * @param robot модель робота <code>Robot</code>
     */
    public RobotVisualizer(Robot robot) {
        this.robot = robot;
    }

    /**
     * <p>Отрисовывает модель робота на входящем графическом контексте</p>
     *
     * @param graphics графический контекст <code>Graphics2D</code>
     */
    public void paint(Graphics2D graphics) {
        int robotCenterX = round(robot.getX());
        int robotCenterY = round(robot.getY());
        AffineTransform t = AffineTransform.getRotateInstance(
                robot.getDirection(),
                round(robot.getX()),
                round(robot.getY())
        ); // поворачивает робота на угол direction относительно координат robotCenterX, robotCenterY

        graphics.setTransform(t);
        graphics.setColor(Color.MAGENTA);
        fillOval(graphics, robotCenterX, robotCenterY, 30, 10);
        graphics.setColor(Color.BLACK);
        drawOval(graphics, robotCenterX, robotCenterY, 30, 10);
        graphics.setColor(Color.WHITE);
        fillOval(graphics, robotCenterX  + 10, robotCenterY, 5, 5);
        graphics.setColor(Color.BLACK);
        drawOval(graphics, robotCenterX  + 10, robotCenterY, 5, 5);
    }
}
