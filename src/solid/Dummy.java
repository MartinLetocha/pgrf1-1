package solid;

import transforms.Point3D;

public class Dummy extends SolidExtended{
    public Dummy() {

        // ========================
        // HEAD (cylinder)
        // ========================
        int headSegments = 8;
        double hr = 0.2;     // radius
        double hHeight = 0.4;
        double hy = 1.2;     // height offset

        int headStart = vertexBuffer.size();

        // back ring
        for (int i = 0; i < headSegments; i++) {
            double ang = Math.PI * 2 * i / headSegments;
            double x = Math.cos(ang) * hr;
            double z = Math.sin(ang) * hr;
            vertexBuffer.add(new Point3D(x, hy + hHeight / 2, z));
        }
        // front ring
        for (int i = 0; i < headSegments; i++) {
            double ang = Math.PI * 2 * i / headSegments;
            double x = Math.cos(ang) * hr;
            double z = Math.sin(ang) * hr;
            vertexBuffer.add(new Point3D(x, hy - hHeight / 2, z));
        }

        for (int i = 0; i < headSegments; i++) {
            int next = (i + 1) % headSegments;

            // back ring
            addIndices(headStart + i, headStart + next);

            // front ring
            addIndices(headStart + headSegments + i,
                    headStart + headSegments + next);

            // connect rings
            addIndices(headStart + i,
                    headStart + headSegments + i);
        }


        // ========================
        // TORSO (box)
        // ========================
        double tw = 0.5, th = 0.6, td = 0.25;
        double ty = 0.4;

        int torsoStart = vertexBuffer.size();
        addBox(torsoStart, -tw, th + ty, -td,  tw, th + ty, -td,
                -tw, ty - th, -td, tw, ty - th, -td);


        // ========================
        // ARMS (2 boxes)
        // ========================
        double aw = 0.15, ah = 1.4, ad = 0.15, offset = 0.1;

        // Left arm
        int armLStart = vertexBuffer.size();
        addBox(armLStart,
                -tw - aw,ty + ah/2 - offset, -ad,
                -tw,ty + ah/2 - offset,  -ad,
                -tw - aw,ty - ah/2 - offset, -ad,
                -tw,ty - ah/2 - offset,  -ad);

        // Right arm
        int armRStart = vertexBuffer.size();
        addBox(armRStart,
                tw,ty + ah/2 - offset, -ad,
                tw + aw,ty + ah/2 - offset, -ad,
                tw,ty - ah/2 - offset, -ad,
                tw + aw,ty - ah/2 - offset, -ad);


        // ========================
        // LEGS (single merged block)
        // ========================
        double lw = 0.3, lh = 0.7, ld = 0.2;
        double ly = -0.9;

        int legsStart = vertexBuffer.size();
        addBox(legsStart,
                -lw, ly + lh, -ld,
                lw, ly + lh,  -ld,
                -lw, ly - lh, -ld,
                lw, ly - lh,  -ld);


        setColor(0xFFFF00); // bright yellow target
    }


    // Helper: Add edges for a rectangular box defined by 8 corners
    private void addBox(int start,
                        double x1, double y1, double z1,
                        double x2, double y2, double z2,
                        double x3, double y3, double z3,
                        double x4, double y4, double z4) {

        // Front face vertices
        vertexBuffer.add(new Point3D(x1, y1, z1)); // 0
        vertexBuffer.add(new Point3D(x2, y2, z2)); // 1
        vertexBuffer.add(new Point3D(x4, y4, z4)); // 2
        vertexBuffer.add(new Point3D(x3, y3, z3)); // 3

        // Back face
        vertexBuffer.add(new Point3D(x1, y1, -z1)); // 4
        vertexBuffer.add(new Point3D(x2, y2, -z2)); // 5
        vertexBuffer.add(new Point3D(x4, y4, -z4)); // 6
        vertexBuffer.add(new Point3D(x3, y3, -z3)); // 7

        addBoxEdges(start);
    }
}
