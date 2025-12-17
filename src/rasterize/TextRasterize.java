package rasterize;

import raster.RasterBufferedImage;

import java.awt.*;

public class TextRasterize extends LineRasterizer{

    public TextRasterize(RasterBufferedImage raster) {
        super(raster);
    }
    public void rasterize(int x, int y, String text) {
        var g = raster.getImage().getGraphics();
        g.drawString(text, x, y);
    }
    public void rasterize(int x, int y, String text, int size) {
        var g = raster.getImage().getGraphics();
        g.setFont(new Font("Arial", Font.PLAIN, size));
        g.drawString(text, x, y);
    }
    public void rasterize(int x, int y, String text, int size, Color color) {
        var g = raster.getImage().getGraphics();
        g.setColor(color);
        g.setFont(new Font("Arial", Font.PLAIN, size));
        g.drawString(text, x, y);
    }
}
