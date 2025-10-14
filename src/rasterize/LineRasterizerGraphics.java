package rasterize;

import raster.RasterBufferedImage;

public class LineRasterizerGraphics extends LineRasterizer {

    public LineRasterizerGraphics(RasterBufferedImage raster) {
        super(raster);
    }
    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int cl) {
        var g = raster.getImage().getGraphics();
        g.drawLine(x1, y1, x2, y2);
    }
}
