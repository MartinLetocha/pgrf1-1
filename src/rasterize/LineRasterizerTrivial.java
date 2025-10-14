package rasterize;

import raster.RasterBufferedImage;

/*
        Triviální algoritmus
        + používá jednoduchou matiku
        - nemá antialiasing
 */
public class LineRasterizerTrivial extends LineRasterizer {

    public LineRasterizerTrivial(RasterBufferedImage raster) {
        super(raster);
    }
    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int cl) {
        if(cl == -1)
        {
            isInterpolation = false;
            isRainbow = true;
        }
        else if(cl == -2)
        {
            isInterpolation = true;
            isRainbow = false;
        }
        if(x1 == x2 && y1 == y2) {
            raster.setPixel(x1, y1, cl);
        }
        if(x1 == x2)
        {
            if (y1 > y2)
            {
                for (int i = y2; i < y1; i++) {
                    raster.setPixel(x1, i, getColor(cl, i, y2, y1));
                }
            }
            else {
                for (int i = y1; i < y2; i++) {
                    raster.setPixel(x1, i, getColor(cl, i, y1, y2));
                }
            }
            return;
        }
        if(x1 > x2)
        {
            int temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        float k = (float) (y2 - y1) / (x2 - x1);
        float q = y1 - k * x1;
        if(k > 1)
        {
            for (int i = y1; i <= y2; i++) {
                raster.setPixel((int) ((i - q) / k), i, getColor(cl, i, y1, y2));
            }
        }
        else if(k < -1)
        {
            int temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
            k = (float) (y2 - y1) / (x2 - x1);
            q = y1 - k * x1;
            for (int i = y1; i <= y2; i++) {
                raster.setPixel((int) ((i - q) / k), i, getColor(cl, i, y1, y2));
            }
        }
        else {
            for (int i = x1; i <= x2; i++) {
                raster.setPixel(i, (int) (k * i + q), getColor(cl, i, x1, x2));
            }
        }
        isInterpolation = false;
        isRainbow = false;
    }
}
