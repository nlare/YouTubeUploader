package com.uploader;

import java.awt.*;
import java.awt.geom.*;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.*;

class RoundedCornerBorder extends AbstractBorder {
    @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int r = height - 1;
        Shape border = new RoundRectangle2D.Double(x, y, width - 1, height - 1, r, r);
        Container parent = c.getParent();
        if (Objects.nonNull(parent)) {
            g2.setPaint(parent.getBackground());
            Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
            corner.subtract(new Area(border));
            g2.fill(corner);
        }
        g2.setPaint(Color.GRAY);
        g2.draw(border);
        g2.dispose();
    }
    @Override public Insets getBorderInsets(Component c) {
        return new Insets(4, 8, 4, 8);
    }
    @Override public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(4, 8, 4, 8);
        return insets;
    }
}