package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {

    private BufferedImage raster;
    private int _width;
    private int _height;

    public Panel(int width, int height) {
        _width = width;
        _height = height;
        setPreferredSize(new Dimension(width, height));
        raster = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(raster, 0, 0, null);
    }
    public void cleanRaster() {
        raster = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        repaint();
    }
    public BufferedImage getRaster() {
        return raster;
    }
}
