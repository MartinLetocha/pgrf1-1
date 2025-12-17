package solid;

import transforms.Point3D;
import transforms.Vec3D;

public class Gun extends SolidExtended {
    public Gun() {

        setColor(0x444444);
        subdivideLine = 2;
        SolidPart gunBody = new SolidPart();
        double bw = 0.6;  // half-width
        double bh = 0.15; // half-height
        double bd = 0.1;  // half-depth

        int bodyStart = gunBody.vertexBuffer.size();

        // 8 corners of the body
        gunBody.vertexBuffer.add(new Point3D(-bw,  bh, -bd)); // 0
        gunBody.vertexBuffer.add(new Point3D( bw,  bh, -bd)); // 1
        gunBody.vertexBuffer.add(new Point3D( bw, -bh, -bd)); // 2
        gunBody.vertexBuffer.add(new Point3D(-bw, -bh, -bd)); // 3

        gunBody.vertexBuffer.add(new Point3D(-bw,  bh,  bd)); // 4
        gunBody.vertexBuffer.add(new Point3D( bw,  bh,  bd)); // 5
        gunBody.vertexBuffer.add(new Point3D( bw, -bh,  bd)); // 6
        gunBody.vertexBuffer.add(new Point3D(-bw, -bh,  bd)); // 7

        // Body edges
        gunBody.addBoxEdges(bodyStart);


        SolidPart handle = new SolidPart();
        double hw = 0.12;
        double hh = 0.3;
        double hd = 0.1;

        int handleStart = handle.vertexBuffer.size();

        // Shift handle down and slightly back
        double hx = -0.3;
        double hy = -0.3 - bh; // below body
        double hz = 0;

        handle.vertexBuffer.add(new Point3D(hx - hw, hy + hh, hz - hd)); // 0
        handle.vertexBuffer.add(new Point3D(hx + hw, hy + hh, hz - hd)); // 1
        handle.vertexBuffer.add(new Point3D(hx + hw, hy - hh, hz - hd)); // 2
        handle.vertexBuffer.add(new Point3D(hx - hw, hy - hh, hz - hd)); // 3

        handle.vertexBuffer.add(new Point3D(hx - hw, hy + hh, hz + hd)); // 4
        handle.vertexBuffer.add(new Point3D(hx + hw, hy + hh, hz + hd)); // 5
        handle.vertexBuffer.add(new Point3D(hx + hw, hy - hh, hz + hd)); // 6
        handle.vertexBuffer.add(new Point3D(hx - hw, hy - hh, hz + hd)); // 7

        // Handle edges
        handle.addBoxEdges(handleStart);


        SolidPart barrel = new SolidPart();
        int barrelSegments = 8;
        double r = 0.1;      // radius
        double length = 0.6; // barrel length
        double bx = bw; // front of gun body

        int barrelStart = barrel.vertexBuffer.size();

        // Two rings: back and front
        for (int i = 0; i < barrelSegments; i++) {
            double angle = (Math.PI * 2.0 * i) / barrelSegments;
            double x = bx;
            double y = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;

            // Back ring
            barrel.vertexBuffer.add(new Point3D(x, y, z));
        }
        for (int i = 0; i < barrelSegments; i++) {
            double angle = (Math.PI * 2.0 * i) / barrelSegments;
            double x = bx + length;
            double y = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;

            // Front ring
            barrel.vertexBuffer.add(new Point3D(x, y, z));
        }

        // Cylinder edges
        for (int i = 0; i < barrelSegments; i++) {
            int next = (i + 1) % barrelSegments;

            // Connect back ring
            barrel.addIndices(barrelStart + i, barrelStart + next);

            // Connect front ring
            barrel.addIndices(barrelStart + barrelSegments + i,
                    barrelStart + barrelSegments + next);

            // Connect each pair
            barrel.addIndices(barrelStart + i,
                    barrelStart + barrelSegments + i);
        }


        barrel.setColor(0x444444);
        handle.setColor(0x444444);
        gunBody.setColor(0x444444);

        parts.add(barrel);
        parts.add(handle);
        parts.add(gunBody);
        createTotal();
    }
}
