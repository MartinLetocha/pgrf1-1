package objects;

public class Point {
    public int X;
    public int Y;
    public Point(int x, int y) {
        X = x;
        Y = y;
    }
    public Point(Point p) {
        X = p.X;
        Y = p.Y;
    }
}
