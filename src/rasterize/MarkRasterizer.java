package rasterize;

import objects.Point;

public class MarkRasterizer {
    public static void rasterizeMark(Point middlePoint, LineRasterizer lineRasterizer)
    {
        lineRasterizer.rasterize(middlePoint.X - 3, middlePoint.Y - 3, middlePoint.X + 3, middlePoint.Y + 3, 0xFF0000);
        lineRasterizer.rasterize(middlePoint.X + 3, middlePoint.Y - 3, middlePoint.X - 3, middlePoint.Y + 3, 0xFF0000);
    }
}
