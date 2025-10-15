package special;

import objects.Line;
import objects.Point;

public class LineAligner {
    public static Line alignLine(int x, int y, Line activeLine)
    {
        boolean pointingLeft = activeLine.Start.X > x;
        float k = (float) (y - activeLine.Start.Y) / (x - activeLine.Start.X);
        float length = (float) Math.sqrt(Math.pow((x - activeLine.Start.X), 2) + Math.pow((y - activeLine.Start.Y), 2));
        var fortyFiveDegreeVector = length * (1 / (Math.sqrt(1 + Math.pow(1, 2))));
        var fortyFiveDegreeVectorMinus = length * (1 / (Math.sqrt(1 + Math.pow(-1, 2))));
        var fortyFiveDegreeVectorMinusVersionTwo = length * (-1 / (Math.sqrt(1 + Math.pow(-1, 2))));
        if (pointingLeft && k >= 0.5 && k < 2.5) // left, 45deg
        {
            int x2 = (int) (activeLine.Start.X - fortyFiveDegreeVector);
            int y2 = (int) (activeLine.Start.Y - fortyFiveDegreeVector);
            activeLine.End = new Point(x2, y2);
        } else if (pointingLeft && k <= -0.5 && k > -2.5) //left, -45deg
        {
            int x2 = (int) (activeLine.Start.X - fortyFiveDegreeVectorMinus);
            int y2 = (int) (activeLine.Start.Y - fortyFiveDegreeVectorMinusVersionTwo);
            activeLine.End = new Point(x2, y2);
        } else if (!pointingLeft && k >= 0.5 && k < 2.5) //right, 45deg
        {
            int x2 = (int) (activeLine.Start.X + fortyFiveDegreeVector);
            int y2 = (int) (activeLine.Start.Y + fortyFiveDegreeVector);
            activeLine.End = new Point(x2, y2);
        } else if (!pointingLeft && k <= -0.5 && k > -2.5) //right, -45deg
        {
            int x2 = (int) (activeLine.Start.X + fortyFiveDegreeVectorMinus);
            int y2 = (int) (activeLine.Start.Y + fortyFiveDegreeVectorMinusVersionTwo);
            activeLine.End = new Point(x2, y2);
        } else if (k < 0.5 && k > -0.5) // left and right, 0deg
        {
            activeLine.End = new Point(x, activeLine.Start.Y);
        } else // top and down, 90deg
        {
            activeLine.End = new Point(activeLine.Start.X, y);
        }
        return activeLine;
    }
}
