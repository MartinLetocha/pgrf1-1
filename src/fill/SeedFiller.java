package fill;

import controller.FillMode;
import objects.Point;
import raster.Raster;

import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;

public class SeedFiller implements Filler{
    public FillMode mode;
    private int fillColor;
    private int backgroundColor;
    private Raster raster;
    private Point startPoint;

    public SeedFiller(Raster raster, Point startPoint, int fillColor, FillMode mode) {
        this.raster = raster;
        this.startPoint = startPoint;
        this.fillColor = fillColor;
        this.mode = mode;
        OptionalInt optionalInt = raster.getPixel(startPoint);
        if(optionalInt.isPresent()) {
            this.backgroundColor = optionalInt.getAsInt();
        }
    }

    @Override
    public void fill() {
        seedFill(startPoint);
    }

    @Override
    public FillMode getFillMode() {
        return mode;
    }

    private void seedFill(int x, int y)
    {
        OptionalInt color = raster.getPixel(x, y);
        if(color.isEmpty())
            return;
        if(color.getAsInt() != backgroundColor || color.getAsInt() == fillColor)
        {
            return;
        }
        raster.setPixel(x, y, fillColor);
        seedFill(x + 1, y);
        seedFill(x - 1, y);
        seedFill(x, y + 1);
        seedFill(x, y - 1);
    }
    private void seedFill(Point point)
    {
        seedFill(point.X, point.Y);
    }
}
