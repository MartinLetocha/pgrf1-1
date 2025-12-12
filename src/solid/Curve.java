package solid;

import transforms.Cubic;
import transforms.Point3D;

public class Curve extends Solid{
    public Curve()
    {
        setColor(0x44eddf);
        Cubic cubic = new Cubic(Cubic.FERGUSON,new Point3D(0,0,0), new Point3D(0.25,0,0.25), new Point3D(0.65,0,1.25), new Point3D(1,0,0));
        int n = 100;
        for (int i = 0; i < n; i++) {
            float step = i / (float)n;
            vertexBuffer.add(cubic.compute(step));
        }
        for (int i = 0; i < vertexBuffer.size() - 1; i++) {
            indexBuffer.add(i);
            indexBuffer.add(i + 1);
        }
    }
    public void recalculate(CurveType type)
    {
        vertexBuffer.clear();
        indexBuffer.clear();
        setColor(0x44eddf);
        Cubic cubic = switch (type) {
            case Ferguson ->
                    new Cubic(Cubic.FERGUSON, new Point3D(0, 0, 0), new Point3D(0.25, 0, 0.25), new Point3D(0.65, 0, 1.25), new Point3D(1, 0, 0));
            case Coons ->
                    new Cubic(Cubic.COONS, new Point3D(0, 0, 0), new Point3D(0.25, 0, 0.25), new Point3D(0.65, 0, 1.25), new Point3D(1, 0, 0));
            default ->
                    new Cubic(Cubic.BEZIER, new Point3D(0, 0, 0), new Point3D(0.25, 0, 0.25), new Point3D(0.65, 0, 1.25), new Point3D(1, 0, 0));
        };

        int n = 100;
        for (int i = 0; i < n; i++) {
            float step = i / (float)n;
            vertexBuffer.add(cubic.compute(step));
        }
        for (int i = 0; i < vertexBuffer.size() - 1; i++) {
            indexBuffer.add(i);
            indexBuffer.add(i + 1);
        }
    }
}
