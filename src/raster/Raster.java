package raster;

import objects.Point;

import java.awt.image.BufferedImage;
import java.util.OptionalInt;

public interface Raster {
    BufferedImage getImage();
    void setPixel(int x, int y, int color);
    OptionalInt getPixel(int x, int y);
    OptionalInt getPixel(Point point);
    int getWidth();
    int getHeight();
    void clear();

    OptionalInt getPixelAlt(int x, int y);
}
