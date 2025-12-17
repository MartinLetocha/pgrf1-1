package solid;

import models.Point3DFix;
import transforms.Point3D;

public class Ray extends SolidExtended {
    public Ray(Point3DFix p1, Point3DFix p2)
    {
        setColor(0x00aaff);
        subdivideLine = 120;
        SolidPart main = new SolidPart();
        main.subdivideLine = 120;
        main.vertexBuffer.add(new Point3D(p1.x, p1.y, p1.z));
        main.vertexBuffer.add(new Point3D(p2.x, p2.y, p2.z));
        main.addIndices(0,1);
        parts.add(main);
        createTotal();
    }
}
