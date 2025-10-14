package raster;

import java.awt.image.BufferedImage;

public class RasterBufferedImage implements Raster {
    private BufferedImage image;
    private int width;
    private int height;

    public RasterBufferedImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }
    public RasterBufferedImage(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void setPixel(int x, int y, int color) {
        if(x < 0 || x >= width || y < 0 || y >= height)
            return;
        image.setRGB(x, y, color);
    }

    @Override
    public int getPixel(int x, int y) {
        return image.getRGB(x, y);
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
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
}
