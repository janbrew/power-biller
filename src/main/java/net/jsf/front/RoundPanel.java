package net.jsf.front;

import javax.swing.*;
import java.awt.*;

public class RoundPanel extends JPanel {
    private Color color1, color2;
    private int radius;

    public RoundPanel(Color color1, Color color2, int radius) {
        this.color1 = color1;
        this.color2 = color2;
        this.radius = radius;
    }

    @Override
    protected void paintChildren(Graphics graphic) {
        Graphics2D graphic2d = (Graphics2D) graphic;
        graphic2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, this.color1, 0, getHeight(), this.color2);
        
        graphic2d.setPaint(gradient);
        graphic2d.fillRoundRect(0, 0, getWidth(), getHeight(), this.radius, this.radius);
        graphic2d.setPaint(gradient);
        graphic2d.drawRoundRect(0, 0, getWidth(), getHeight(), this.radius, this.radius);
        super.paintChildren(graphic);
    }
}
