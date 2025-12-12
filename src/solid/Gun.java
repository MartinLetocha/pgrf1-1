package solid;

import transforms.Point3D;
import transforms.Vec3D;

public class Gun extends Solid {
    public Gun() {

        // ========================
        // BLOCK 1 — GUN BODY
        // ========================
        double bw = 0.6;  // half-width
        double bh = 0.15; // half-height
        double bd = 0.1;  // half-depth

        int bodyStart = vertexBuffer.size();

        // 8 corners of the body
        vertexBuffer.add(new Point3D(-bw,  bh, -bd)); // 0
        vertexBuffer.add(new Point3D( bw,  bh, -bd)); // 1
        vertexBuffer.add(new Point3D( bw, -bh, -bd)); // 2
        vertexBuffer.add(new Point3D(-bw, -bh, -bd)); // 3

        vertexBuffer.add(new Point3D(-bw,  bh,  bd)); // 4
        vertexBuffer.add(new Point3D( bw,  bh,  bd)); // 5
        vertexBuffer.add(new Point3D( bw, -bh,  bd)); // 6
        vertexBuffer.add(new Point3D(-bw, -bh,  bd)); // 7

        // Body edges
        addBoxEdges(bodyStart);


        // ========================
        // BLOCK 2 — HANDLE
        // ========================
        double hw = 0.12;
        double hh = 0.3;
        double hd = 0.1;

        int handleStart = vertexBuffer.size();

        // Shift handle down and slightly back
        double hx = -0.3;
        double hy = -0.3 - bh; // below body
        double hz = 0;

        vertexBuffer.add(new Point3D(hx - hw, hy + hh, hz - hd)); // 0
        vertexBuffer.add(new Point3D(hx + hw, hy + hh, hz - hd)); // 1
        vertexBuffer.add(new Point3D(hx + hw, hy - hh, hz - hd)); // 2
        vertexBuffer.add(new Point3D(hx - hw, hy - hh, hz - hd)); // 3

        vertexBuffer.add(new Point3D(hx - hw, hy + hh, hz + hd)); // 4
        vertexBuffer.add(new Point3D(hx + hw, hy + hh, hz + hd)); // 5
        vertexBuffer.add(new Point3D(hx + hw, hy - hh, hz + hd)); // 6
        vertexBuffer.add(new Point3D(hx - hw, hy - hh, hz + hd)); // 7

        // Handle edges
        addBoxEdges(handleStart);


        // ========================
        // CYLINDER BARREL
        // ========================
        int barrelSegments = 8;
        double r = 0.1;      // radius
        double length = 0.6; // barrel length
        double bx = bw; // front of gun body

        int barrelStart = vertexBuffer.size();

        // Two rings: back and front
        for (int i = 0; i < barrelSegments; i++) {
            double angle = (Math.PI * 2.0 * i) / barrelSegments;
            double x = bx;
            double y = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;

            // Back ring
            vertexBuffer.add(new Point3D(x, y, z));
        }
        for (int i = 0; i < barrelSegments; i++) {
            double angle = (Math.PI * 2.0 * i) / barrelSegments;
            double x = bx + length;
            double y = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;

            // Front ring
            vertexBuffer.add(new Point3D(x, y, z));
        }

        // Cylinder edges
        for (int i = 0; i < barrelSegments; i++) {
            int next = (i + 1) % barrelSegments;

            // Connect back ring
            addIndices(barrelStart + i, barrelStart + next);

            // Connect front ring
            addIndices(barrelStart + barrelSegments + i,
                    barrelStart + barrelSegments + next);

            // Connect each pair
            addIndices(barrelStart + i,
                    barrelStart + barrelSegments + i);
        }

        setColor(0x444444);
    }
}
