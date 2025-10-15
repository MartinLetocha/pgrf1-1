package rasterize;

import objects.Point;
import raster.RasterBufferedImage;
import special.ColorGradient;

import java.awt.*;

public abstract class LineRasterizer {
    protected RasterBufferedImage raster;
    protected boolean isRainbow;
    protected boolean isInterpolation;
    private ColorGradient colorGradient;

    public LineRasterizer(RasterBufferedImage raster) {
        this.raster = raster;
    }

    public void rasterize(int x1, int y1, int x2, int y2, int color) {

    }

    public void rasterize(Point start, Point end, int color) {
        rasterize(start.X, start.Y, end.X, end.Y, color);
    }
    public void setColorGradient(ColorGradient colorGradient) {
        this.colorGradient = colorGradient;
    }

    public int getColor(int color, int index, int start, int max) {
        if (isRainbow) {
            float t = (float)(index - start) / (max - start);
            return Color.HSBtoRGB(t, 1f, 1f);
        } else if (isInterpolation) {
            if(colorGradient != null) {
                float t = (float) (index - start) / (max - start);
                return LerpHSB(Color.RGBtoHSB(colorGradient.startColor.getRed(), colorGradient.startColor.getGreen(), colorGradient.startColor.getBlue(), null), Color.RGBtoHSB(colorGradient.endColor.getRed(), colorGradient.endColor.getGreen(), colorGradient.endColor.getBlue(), null), t);
            }
            return 0xAAFFAA;
        } else {
            return color;
        }
    }

    public int LerpHSB(float[] s, float[] e, float t) {
        float[] start = s.clone();
        float[] end = e.clone();
        float h = 0;
        float d = end[0] - start[0];
        if (start[0] > end[0]) {
            var h3 = end[0];
            end[0] = start[0];
            start[0] = h3;
            d = -d;
            t = 1 - t;
        }
        if (d > 0.5) {
            start[0] = start[0] + t;
            h = (start[0] + t * (end[0] - start[0])) % 1;
        }
        if (d <= 0.5) {
            h = start[0] + t * d;
        }
        float[] tColor = new float[]
                {h,
                        start[1] + t * (end[1] - start[1]),
                        start[2] + t * (end[2] - start[2])
                };
        return Color.HSBtoRGB(tColor[0], tColor[1], tColor[2]);
    }
}
