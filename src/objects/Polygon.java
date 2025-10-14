package objects;

import special.ColorGradient;

import java.util.ArrayList;

public class Polygon {
    private ArrayList<Point> points;
    public int Color;
    public ColorGradient colorGradient;
    public Polygon(int Color) {
        this.points = new ArrayList<>();
        this.Color = Color;
    }
    public void addPoint(Point point) {
        this.points.add(point);
    }
    public Point getPoint(int index) {
        return this.points.get(index);
    }
    public int getPointsSize()
    {
        return this.points.size();
    }
}
