package rasterize;

import raster.RasterBufferedImage;

public class TextRasterize extends LineRasterizer{

    public TextRasterize(RasterBufferedImage raster) {
        super(raster);
    }
    public void rasterize(int x, int y, String text) {
        var g = raster.getImage().getGraphics();
        g.drawString(text, x, y);
    }
}
