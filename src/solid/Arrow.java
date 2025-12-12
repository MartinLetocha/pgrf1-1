package solid;

import transforms.Point3D;

public class Arrow extends Solid{
    public Arrow() {
        vertexBuffer.add(new Point3D(-0.5, 0, 0));
        vertexBuffer.add(new Point3D(0.4, 0, 0));
        vertexBuffer.add(new Point3D(0.4, -0.1, 0));
        vertexBuffer.add(new Point3D(0.5, 0, 0));
        vertexBuffer.add(new Point3D(0.4, 0.1, 0));

        addIndices(0,1);
        addIndices(2,3);
        addIndices(3,4);
        addIndices(4,2);

        setColor(0xFFFFFF);
    }
}
