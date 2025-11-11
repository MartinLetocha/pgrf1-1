package objects;

import java.util.ArrayList;

public class Rectangle extends Polygon {

    public Rectangle(int Color) {
        super(Color);
    }
    public void makeRectangle(Point H) {
        Point A = getPoint(0);
        Point B = getPoint(1);

        float vx = B.X - A.X;
        float vy = B.Y - A.Y;
        float baseLength = (float) Math.sqrt(vx * vx + vy * vy);

        vx /= baseLength;
        vy /= baseLength;

        float hx = H.X - A.X;
        float hy = H.Y - A.Y;
        float px = -vy;
        float py = vx;
        float rectHeight = hx * px + hy * py;

        Point D = new Point(
                Math.round(A.X + px * rectHeight),
                Math.round(A.Y + py * rectHeight)
        );
        Point C = new Point(
                Math.round(B.X + px * rectHeight),
                Math.round(B.Y + py * rectHeight)
        );

        addPoint(C);
        addPoint(D);
    }
}
