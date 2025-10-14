package rasterize;

import objects.Point;
import raster.RasterBufferedImage;

/*
       Xiaolin Wu
       + Poměrně jednoduché
       + Čistě matematické
       - Potřebuje řešit desetinná čísla
       - Nezvládá tlustší čáry
*/
public class FilledLineRasterizer extends LineRasterizer {
    public FilledLineRasterizer(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {
        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);

        if (steep) {
            int tmp;
            tmp = x1;
            x1 = y1;
            y1 = tmp;
            tmp = x2;
            x2 = y2;
            y2 = tmp;
        }

        if (x1 > x2) {
            int tmp;
            tmp = x1;
            x1 = x2;
            x2 = tmp;
            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }

        float dx = x2 - x1;
        float dy = y2 - y1;
        float gradient = (dx == 0) ? 1 : dy / dx;

        float xend = Math.round(x1);
        float yend = y1 + gradient * (xend - x1);
        float xgap = rfpart(x1 + 0.5f);
        int xpxl1 = (int)xend;
        int ypxl1 = (int)Math.floor(yend);

        if (steep) {
            plot(ypxl1,   xpxl1, rfpart(yend) * xgap, color);
            plot(ypxl1+1, xpxl1, fpart(yend)  * xgap, color);
        } else {
            plot(xpxl1, ypxl1,   rfpart(yend) * xgap, color);
            plot(xpxl1, ypxl1+1, fpart(yend)  * xgap, color);
        }

        float intery = yend + gradient;

        // handle second endpoint
        xend = Math.round(x2);
        yend = y2 + gradient * (xend - x2);
        xgap = fpart(x2 + 0.5f);
        int xpxl2 = (int)xend;
        int ypxl2 = (int)Math.floor(yend);

        if (steep) {
            plot(ypxl2,   xpxl2, rfpart(yend) * xgap, color);
            plot(ypxl2+1, xpxl2, fpart(yend)  * xgap, color);
        } else {
            plot(xpxl2, ypxl2,   rfpart(yend) * xgap, color);
            plot(xpxl2, ypxl2+1, fpart(yend)  * xgap, color);
        }

        // main loop
        for (int x = xpxl1 + 1; x < xpxl2; x++) {
            if (steep) {
                plot((int)Math.floor(intery), x, rfpart(intery), color);
                plot((int)Math.floor(intery)+1, x, fpart(intery), color);
            } else {
                plot(x, (int)Math.floor(intery), rfpart(intery), color);
                plot(x, (int)Math.floor(intery)+1, fpart(intery), color);
            }
            intery += gradient;
        }
    }
    float fpart(float x) {
        return x - (float)Math.floor(x);
    }

    float rfpart(float x) {
        return 1 - fpart(x);
    }
    void plot(int x, int y, float brightness, int baseColor) {
        int r = (int)(((baseColor >> 16) & 0xFF) * brightness);
        int g = (int)(((baseColor >> 8) & 0xFF) * brightness);
        int b = (int)((baseColor & 0xFF) * brightness);
        int rgb = (r << 16) | (g << 8) | b;

        raster.setPixel(x, y, rgb);
    }
}
