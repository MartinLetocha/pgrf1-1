package fill;

import controller.FillMode;
import objects.Point;
import raster.Raster;

import java.awt.*;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;

public class SeedFiller implements Filler {
    public FillMode mode;
    private int fillColor;
    private int backgroundColor;
    private OptionalInt borderColor;
    private Raster raster;
    private Point startPoint;

    public SeedFiller(Raster raster, Point startPoint, int fillColor, OptionalInt borderColor, FillMode mode) {
        this.raster = raster;
        this.startPoint = startPoint;
        this.fillColor = fillColor;
        this.mode = mode;
        this.borderColor = borderColor;
        OptionalInt optionalInt = raster.getPixelAlt(startPoint.X, startPoint.Y);
        if (optionalInt.isPresent()) {
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

    private void seedFill(int x, int y) {
        OptionalInt color = raster.getPixelAlt(x, y);
        if (color.isEmpty())
            return;
        Color converter = new Color(color.getAsInt());
        Color converterFill = new Color(fillColor);
        Color converterBG = new Color(backgroundColor);
        int convertedBackground = converterBG.getRGB();
        int convertedFill = converterFill.getRGB();
        int convertedColor = converter.getRGB();
        if (borderColor.isEmpty()) {
            if (convertedColor != convertedBackground || convertedColor == convertedFill) {
                return;
            }
        } else {
            if (convertedColor == borderColor.getAsInt() || convertedColor == convertedFill) {
                return;
            }
        }
        raster.setPixel(x, y, fillColor);
        seedFill(x + 1, y);
        seedFill(x - 1, y);
        seedFill(x, y + 1);
        seedFill(x, y - 1);
    }

    private void seedFill(Point point) {
        seedFill(point.X, point.Y);
    }
}
