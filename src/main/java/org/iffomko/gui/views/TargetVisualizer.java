package org.iffomko.gui.views;

import org.iffomko.gui.models.Target;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * <p>Отрисовывает модель цели для робота <code>Target</code></p>
 */
public class TargetVisualizer extends Visualizer {
    private final Target target;

    /**
     * Инициализирует поле, содержащее модель цели для робота
     * @param target модель цели для робота <code>Target</code>
     */
    public TargetVisualizer(Target target) {
        this.target = target;
    }

    /**
     * <p>Отрисовывает модель цели для робота на входящем графическом контексте</p>
     *
     * @param graphics графический контекст <code>Graphics2D</code>
     */
    @Override
    public void paint(Graphics2D graphics) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); // поворачивает на ноль градусов указатель
        graphics.setTransform(t);
        graphics.setColor(Color.GREEN);
        fillOval(graphics, target.getX(), target.getY(), 5, 5);
        graphics.setColor(Color.BLACK);
        drawOval(graphics, target.getX(), target.getY(), 5, 5);
    }
}
