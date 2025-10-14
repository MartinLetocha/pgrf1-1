package raster;

import java.awt.image.BufferedImage;

public interface Raster {
    BufferedImage getImage();
    void setPixel(int x, int y, int color);
    int getPixel(int x, int y);
    int getWidth();
    int getHeight();
    void clear();
}
