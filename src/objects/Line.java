package objects;

import special.ColorGradient;

public class Line {
    public Point Start;
    public Point End;
    public int Color;
    public ColorGradient colorGradient;
    public Line(Point st, Point ed, int c) {
        Start = st;
        End = ed;
        Color = c;
    }
}
