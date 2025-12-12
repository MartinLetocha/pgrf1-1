package solid;

import transforms.Point3D;

public class Axis extends Solid {
    public Axis(Direction direction) {
        switch (direction) {
            case X:
                vertexBuffer.add(new Point3D(0, 0, 0));
                vertexBuffer.add(new Point3D(1, 0, 0));
                setColor(0xFF0000);
                break;
            case Y:
                vertexBuffer.add(new Point3D(0, 0, 0));
                vertexBuffer.add(new Point3D(0, 1, 0));
                setColor(0x00FF00);
                break;
            case Z:
                vertexBuffer.add(new Point3D(0, 0, 0));
                vertexBuffer.add(new Point3D(0, 0, 1));
                setColor(0x0000FF);
                break;
        }
        addIndices(0,1);
    }
}
