package objects;

import special.ColorGradient;

public class Edge {
    public Point Start;
    public Point End;
    public int Color;
    public ColorGradient colorGradient;
    public Edge(Point st, Point ed, int c) {
        Start = st;
        End = ed;
        Color = c;
    }
    public Edge(Point st, Point ed) {
        Start = st;
        End = ed;
    }
    public boolean isHorizontal()
    {
        return Start.Y == End.Y;
    }
    public void orientate()
    {
        if(Start.Y > End.Y)
        {
            Point temp = End;
            End = Start;
            Start = temp;
        }
    }
    public boolean isIntersection(int y)
    {
        return Start.Y <= y && y < End.Y;
    }
    public int getIntersection(int y)
    {
        float k = (float) (End.X - Start.X) / (End.Y - Start.Y);
        float q = Start.X - k * Start.Y;
        return (int) (k * y + q);
    }
}
