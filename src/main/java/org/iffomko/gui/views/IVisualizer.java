package org.iffomko.gui.views;

import java.awt.*;

/**
 * <p>Класс, который умеет отрисовывать какую-то модель на графическом контексте</p>
 */
public interface IVisualizer {
    /**
     * <p>Отрисовывает модель на входящем графическом контексте</p>
     * @param graphics графический контекст <code>Graphics2D</code>
     */
    void paint(Graphics2D graphics);
}
