package clip;

import objects.Edge;
import objects.Point;
import objects.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Clipper
{
    public ArrayList<Point> clip(Polygon cuttingPolygon, Polygon polygonToBeCut)
    {
        var clipPoints = cuttingPolygon.getPoints();
        var points = polygonToBeCut.getPoints();
        if (clipPoints.size() < 2)
            return points;
        ArrayList<Point> newPoints = (ArrayList<Point>) points.clone();
        Point lastCuttingPoint = clipPoints.get(clipPoints.size() - 1);
        for (Point cuttingPoint : clipPoints) {
            if (points.size() < 2) newPoints = (ArrayList<Point>) points.clone();
            else {
                newPoints.clear();
                Point lastPointToBeCut = points.get(points.size() - 1);
                Edge edge = new Edge(lastCuttingPoint, cuttingPoint);
                for (Point pointToBeCut : points) {
                    if (isInside(pointToBeCut, edge)) {
                        if (!isInside(lastPointToBeCut, edge)) {
                            newPoints.add(intersection(lastPointToBeCut, pointToBeCut, lastCuttingPoint, cuttingPoint));
                        }
                        newPoints.add(pointToBeCut);
                    } else if (isInside(lastPointToBeCut, edge)) {
                        newPoints.add(intersection(lastPointToBeCut, pointToBeCut, lastCuttingPoint, cuttingPoint));
                    }
                    lastPointToBeCut = pointToBeCut;
                }
            }
            points = (ArrayList<Point>) newPoints.clone();
            lastCuttingPoint = cuttingPoint;
        }
        return newPoints;
    }
    public boolean isInside(Point p, Edge edge) {
        Point vectorEdge = new Point(edge.End.X - edge.Start.X, edge.End.Y - edge.Start.Y);
        Point normal = new Point(-vectorEdge.Y, vectorEdge.X);
        Point vector = new Point(p.X - edge.Start.X, p.Y - edge.Start.Y);
        return (normal.X * vector.X + normal.Y * vector.Y < 0.0f);
    }
    Point intersection(Point p1, Point p2, Point p3, Point p4) {
        double dx = (p1.X - p2.X);
        double dx2 = (p3.X - p4.X);
        double dy = (p1.Y - p2.Y);
        double dy2 = (p3.Y - p4.Y);
        double between = (p1.X * p2.Y - p1.Y * p2.X);
        double between2 = (p3.X * p4.Y - p3.Y * p4.X);
        double px = (between * dx2 - between2 * dx) / (dx * dy2 - dx2 * dy);
        double py = (between * dy2 - between2 * dy) / (dx * dy2 - dx2 * dy);
        return new Point((int) px, (int) py);
    }
}
