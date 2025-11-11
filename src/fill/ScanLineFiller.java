package fill;

import controller.FillMode;
import objects.Edge;
import objects.Point;
import objects.Polygon;
import rasterize.LineRasterizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static rasterize.PolygonRasterizer.drawPolygon;

public class ScanLineFiller implements Filler {
    public FillMode mode;
    private Polygon polygon;
    private LineRasterizer rasterizer;
    private int color;
    public ScanLineFiller(Polygon polygon, LineRasterizer rasterizer, int color, FillMode mode) {
        this.polygon = polygon;
        this.rasterizer = rasterizer;
        this.color = color;
        this.mode = mode;
    }

    @Override
    public void fill() {
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.getPointsSize(); i++) {
            int indexA = i;
            int indexB = (i + 1) % polygon.getPointsSize();
            Point pA = polygon.getPoint(indexA);
            Point pB = polygon.getPoint(indexB);
            Edge edge = new Edge(pA, pB, color);
            if(!edge.isHorizontal()) {
                edge.orientate();
                edges.add(edge);
            }
        }
        int yMin = polygon.getPoint(0).Y;
        int yMax = 0;
        for (int i = 0; i < polygon.getPointsSize(); i++) {
            Point point = polygon.getPoint(i);

            if(point.Y < yMin)
                yMin = point.Y;
            if(point.Y > yMax)
                yMax = point.Y;

            for (int y = yMin; y < yMax; y++) {
                ArrayList<Integer> intersections = new ArrayList<>();
                for (Edge edge : edges) {
                    if(!edge.isIntersection(y))
                        continue;
                    int x = edge.getIntersection(y);
                    intersections.add(x);
                }
                Collections.sort(intersections);
                for (int j = 0; j < intersections.size(); j += 2) {
                    rasterizer.rasterize(intersections.get(j), y, intersections.get(j + 1), y, color);
                }
            }
            drawPolygon(polygon, rasterizer);
        }
    }

    @Override
    public FillMode getFillMode() {
        return mode;
    }
}
