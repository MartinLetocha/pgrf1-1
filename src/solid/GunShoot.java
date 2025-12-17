package solid;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class GunShoot extends SolidExtended{
    public GunShoot()
    {
        setColor(0xffffff);
        subdivideLine = 2;
        parts.add(createRing(0xff0939, 0.4, 0.02, 0.02, 1, 1, 5));
        parts.add(createRing(0xff7500, 0.7, 0.07, 0.1, 0.8, 0.3, 5));
        parts.add(createRing(0xffb970, 0.65, 0.1, 0.15, 0.7, 0.2, 5));
        parts.add(createRing(0xffffff, 0.6, 0.1, 0.15, 0.6, 0.2, 2.5));
        createTotal();
    }
    SolidPart createRing(int color, double start, double ringOneSize, double ringTwoSize, double length, double layerOffset, double rotationPoint)
    {
        SolidPart solidPart = new SolidPart();
        List<Point3D> partVertexBuffer = new ArrayList<>();
        solidPart.color = color;
        int barrelSegments = 8;
        for (int i = 0; i < barrelSegments; i++) {
            double angle = ((Math.PI * 2.0 * i) + rotationPoint - 2.5) / barrelSegments;
            double x = start + length;
            double y = Math.cos(angle) * ringOneSize;
            double z = Math.sin(angle) * ringOneSize;
            partVertexBuffer.add(new Point3D(x, y, z));
        }
        for (int i = 0; i < barrelSegments; i++) {
            double angle2 = ((Math.PI * 2 * i) + rotationPoint) / barrelSegments;
            double x2 = start + length + layerOffset;
            double y2 = Math.cos(angle2) * ringTwoSize;
            double z2 = Math.sin(angle2) * ringTwoSize;
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
