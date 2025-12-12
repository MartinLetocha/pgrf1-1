package models;

public class PointMinMax {
    public Point3DFix min;
    public Point3DFix max;
    public PointMinMax(Point3DFix min, Point3DFix max) {
        this.min = min;
        this.max = max;
    }
    public Point3DFix getOrigin()
    {
        return new Point3DFix((min.x + max.x) * 0.5,(min.y + max.y) * 0.5,(min.z + max.z) * 0.5);
    }
}
