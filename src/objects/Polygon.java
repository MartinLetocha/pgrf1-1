package objects;

import special.ColorGradient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polygon {
    private ArrayList<Point> points;
    public int Color;
    public ColorGradient colorGradient;
    public Polygon(int Color) {
        this.points = new ArrayList<>();
        this.Color = Color;
    }
    public Polygon(ArrayList<Point> points) {
        this.points = points;
    }
    public Polygon(ArrayList<Point> points, int Color) {
        this.points = points;
        this.Color = Color;
    }
    public void reversePoints()
    {
        Collections.reverse(points);
    }
    public void addPoint(Point point) {
        this.points.add(point);
    }
    public void changeLastPoint(Point point)
    {
        this.points.set(this.points.size() - 1, point);
    }
    public Point getPoint(int index) {
        return this.points.get(index);
    }
    public ArrayList<Point> getPoints() {
        return this.points;
    }
    public int getPointsSize()
    {
        return this.points.size();
    }
}
