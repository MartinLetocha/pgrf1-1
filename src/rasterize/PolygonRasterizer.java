package rasterize;

import objects.Polygon;

public class PolygonRasterizer {
    public static void drawPolygon(Polygon polygon, LineRasterizer lineRasterizer) {
        if (polygon == null)
            return;
        int size = polygon.getPointsSize();
        if (size < 3) {
            for (int i = 0; i < size; i++) {
                var middlePoint = polygon.getPoint(i);
                MarkRasterizer.rasterizeMark(middlePoint, lineRasterizer);
            }
            return;
        }
        for (int i = 0; i < size; i++) {
            lineRasterizer.setColorGradient(polygon.colorGradient);
            if (i + 1 >= size) {
                lineRasterizer.rasterize(polygon.getPoint(i), polygon.getPoint(0), polygon.Color);
            } else {
                lineRasterizer.rasterize(polygon.getPoint(i), polygon.getPoint(i + 1), polygon.Color);
            }
        }
    }
}
