package solid;

import transforms.Point3D;

public class Cube extends Solid{
    public Cube() {
        vertexBuffer.add(new Point3D(-0.5,-0.5,-0.5));
        vertexBuffer.add(new Point3D(0.5,-0.5,-0.5));
        vertexBuffer.add(new Point3D(-0.5,0.5,-0.5));
        vertexBuffer.add(new Point3D(-0.5,-0.5,0.5));
        vertexBuffer.add(new Point3D(0.5,0.5,-0.5));
        vertexBuffer.add(new Point3D(-0.5,0.5,0.5));
        vertexBuffer.add(new Point3D(0.5,-0.5,0.5));
        vertexBuffer.add(new Point3D(0.5,0.5,0.5));

        addIndices(0, 1);
        addIndices(0, 2);
        addIndices(0, 3);

        addIndices(1, 4);
        addIndices(1, 6);

        addIndices(2, 4);
        addIndices(2, 5);

        addIndices(3, 6);
        addIndices(3, 5);

        addIndices(4, 7);
        addIndices(5, 7);
        addIndices(6, 7);

        setColor(0xFF00FF);
    }
}
