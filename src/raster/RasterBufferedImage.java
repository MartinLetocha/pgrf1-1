package raster;

import objects.Point;

import java.awt.image.BufferedImage;
import java.util.OptionalInt;

public class RasterBufferedImage implements Raster {
    private BufferedImage image;
    private int[][] alternative;
    private int width;
    private int height;

    public RasterBufferedImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        alternative = new int[width][height];
    }
    public RasterBufferedImage(int width, int height) {
        this.width = width;
        this.height = height;
        alternative = new int[width][height];
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    @Override
    public BufferedImage getImage() {
        return image;
    }
    public void clearAlternative() {
        alternative = new int[width][height];
    }
    @Override
    public void setPixel(int x, int y, int color) {
        if(x < 0 || x >= width || y < 0 || y >= height)
            return;
        alternative[x][y] = color;
        image.setRGB(x, y, color);
    }

    @Override
    public OptionalInt getPixel(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return OptionalInt.empty();
        int color = image.getRGB(x, y);
        return OptionalInt.of(color);
    }

    public OptionalInt getPixelAlt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return OptionalInt.empty();
        int color = alternative[x][y];
        return OptionalInt.of(color);
    }

    @Override
    public OptionalInt getPixel(Point point) {
        return getPixel(point.X, point.Y);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void clear() {
        clearAlternative();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
}
