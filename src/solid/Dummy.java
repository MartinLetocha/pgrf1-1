package solid;

import models.Point3DFix;
import transforms.Point3D;

public class Dummy extends SolidExtended{
    public boolean isHit = false;
    public Dummy() {
        int color = 0xFFFF00;
        subdivideLine = 2;
        setColor(color);
        SolidPart hitBox = new SolidPart();
        hitBox.type = "hitbox";
        double twh = 0.65, thh = 1.5, tdh = 0.25;
        double tyh = -0.1;
        coreBottom = new Point3DFix(0,thh + tyh - 1, 0);
        coreTop = new Point3DFix(0,tyh - thh + 1, 0);

        addBox(hitBox, 0,
                -twh, thh + tyh, -tdh,
                twh, thh + tyh, -tdh,
                -twh, tyh - thh, -tdh,
                twh, tyh - thh, -tdh);

        SolidPart head = new SolidPart();
        int headSegments = 8;
        double hr = 0.2;     // radius
        double hHeight = 0.4;
        double hy = 1.2;     // height offset

        int headStart = head.vertexBuffer.size();

        // back ring
        for (int i = 0; i < headSegments; i++) {
            double ang = Math.PI * 2 * i / headSegments;
            double x = Math.cos(ang) * hr;
            double z = Math.sin(ang) * hr;
            head.vertexBuffer.add(new Point3D(x, hy + hHeight / 2, z));
        }
        // front ring
        for (int i = 0; i < headSegments; i++) {
            double ang = Math.PI * 2 * i / headSegments;
            double x = Math.cos(ang) * hr;
            double z = Math.sin(ang) * hr;
            head.vertexBuffer.add(new Point3D(x, hy - hHeight / 2, z));
        }

        for (int i = 0; i < headSegments; i++) {
            int next = (i + 1) % headSegments;

            // back ring
            head.addIndices(headStart + i, headStart + next);

            // front ring
            head.addIndices(headStart + headSegments + i,
                    headStart + headSegments + next);

            // connect rings
            head.addIndices(headStart + i,
                    headStart + headSegments + i);
        }


        SolidPart torso = new SolidPart();
        torso.type = "Box";
        double tw = 0.5, th = 0.6, td = 0.25;
        double ty = 0.4;
        torso.width = tw;
        torso.height = th + ty;
        torso.depth = td;

        int torsoStart = torso.vertexBuffer.size();
        addBox(torso, torsoStart,
                -tw, th + ty, -td,
                tw, th + ty, -td,
                -tw, ty - th, -td,
                tw, ty - th, -td);


        SolidPart leftArm = new SolidPart();
        SolidPart rightArm = new SolidPart();
        double aw = 0.15, ah = 1.4, ad = 0.15, offset = 0.1;

        int armLStart = leftArm.vertexBuffer.size();
        addBox(leftArm, armLStart,
                -tw - aw,ty + ah/2 - offset, -ad,
                -tw,ty + ah/2 - offset,  -ad,
                -tw - aw,ty - ah/2 - offset, -ad,
                -tw,ty - ah/2 - offset,  -ad);

        int armRStart = rightArm.vertexBuffer.size();
        addBox(rightArm, armRStart,
                tw,ty + ah/2 - offset, -ad,
                tw + aw,ty + ah/2 - offset, -ad,
                tw,ty - ah/2 - offset, -ad,
                tw + aw,ty - ah/2 - offset, -ad);


        SolidPart legs = new SolidPart();
        double lw = 0.3, lh = 0.7, ld = 0.2;
        double ly = -0.9;

        int legsStart = legs.vertexBuffer.size();
        addBox(legs, legsStart,
                -lw, ly + lh, -ld,
                lw, ly + lh,  -ld,
                -lw, ly - lh, -ld,
                lw, ly - lh,  -ld);
        parts.add(head);
        parts.add(torso);
        parts.add(leftArm);
        parts.add(rightArm);
        parts.add(legs);
        parts.add(hitBox);
        createTotal();
        setAllColor(color);
    }


    private void addBox(SolidPart part, int start,
                        double x1, double y1, double z1,
                        double x2, double y2, double z2,
                        double x3, double y3, double z3,
                        double x4, double y4, double z4) {

        part.vertexBuffer.add(new Point3D(x1, y1, z1));
        part.vertexBuffer.add(new Point3D(x2, y2, z2));
        part.vertexBuffer.add(new Point3D(x4, y4, z4));
        part.vertexBuffer.add(new Point3D(x3, y3, z3));

        part.vertexBuffer.add(new Point3D(x1, y1, -z1));
        part.vertexBuffer.add(new Point3D(x2, y2, -z2));
        part.vertexBuffer.add(new Point3D(x4, y4, -z4));
        part.vertexBuffer.add(new Point3D(x3, y3, -z3));

        part.addBoxEdges(start);
    }
}
