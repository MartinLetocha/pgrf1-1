package solid;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class GunShoot extends SolidExtended{
    public GunShoot()
    {
        setColor(0xffffff);
        parts.add(createRing(0xffffff, 0.6, 0.1, 0.15));
        parts.add(createRing(0xff7500, 0.7, 0.07, 0.1));
        createTotal();
    }
    SolidPart createRing(int color, double start, double ringOneSize, double ringTwoSize)
    {
        SolidPart solidPart = new SolidPart();
        List<Point3D> partVertexBuffer = new ArrayList<Point3D>();
        solidPart.color = color;
        int barrelSegments = 8;
        double r = ringOneSize;
        double r2 = ringTwoSize;
        double length = 0.6;
        double bx = start;
        double layerOffset = 0.2;
        for (int i = 0; i < barrelSegments; i++) {
            double angle = (Math.PI * 2.0 * i) / barrelSegments;
            double x = bx + length;
            double y = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;
            partVertexBuffer.add(new Point3D(x, y, z));
        }
        for (int i = 0; i < barrelSegments; i++) {
            double angle2 = ((Math.PI * 2 * i) + 2.5) / barrelSegments;
            double x2 = bx + length + layerOffset;
            double y2 = Math.cos(angle2) * r2;
            double z2 = Math.sin(angle2) * r2;
            partVertexBuffer.add(new Point3D(x2, y2, z2));
        }
        for (int i = 0; i < barrelSegments; i++) {
            if(i + 1 >= barrelSegments)
            {
                solidPart.addIndices(i, 0);

            }
            else {
                solidPart.addIndices(i, i + 1);

            }
        }
        for (int i = 0; i < barrelSegments; i++) {
            if(i + 1 >= barrelSegments)
            {

                solidPart.addIndices(i, i + barrelSegments);
                solidPart.addIndices(i + barrelSegments, 0);
            }
            else {

                solidPart.addIndices(i, i + barrelSegments);
                solidPart.addIndices(i + barrelSegments, i + 1);
            }
        }
        solidPart.vertexBuffer = partVertexBuffer;
        return solidPart;
    }
}
