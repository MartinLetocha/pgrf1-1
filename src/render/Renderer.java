package render;

import rasterize.LineRasterizer;
import solid.Solid;
import solid.SolidExtended;
import solid.SolidPart;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.List;
import java.util.Objects;

public class Renderer {
    private LineRasterizer lineRasterizer;
    private int width, height;
    private Mat4 viewMatrix, projectionMatrix;

    public Renderer(LineRasterizer ln, int width, int height, Mat4 viewMatrix, Mat4 projectionMatrix) {
        this.width = width;
        this.height = height;
        this.lineRasterizer = ln;
        this.viewMatrix = viewMatrix;
        this.projectionMatrix = projectionMatrix;
    }
    public void renderSolidExtended(SolidExtended solid)
    {
        for (SolidPart part : solid.parts)
        {
            if(Objects.equals(part.type, "hitbox"))
                continue;
            part.setSubdivideLine(solid.getSubdivideLine());
            renderSolid(part, solid);
        }
    }
    public void renderSolid(Solid solid)
    {
        for (int i = 0; i < solid.getIndexBuffer().size(); i += 2) {
            int indexA = solid.getIndexBuffer().get(i);
            int indexB = solid.getIndexBuffer().get(i + 1);

            Point3D a = solid.getVertexBuffer().get(indexA);
            Point3D b = solid.getVertexBuffer().get(indexB);

            //transformations, MVP
            a = a.mul(solid.getModelMatrix());
            b = b.mul(solid.getModelMatrix());

            a = a.mul(viewMatrix);
            b = b.mul(viewMatrix);

            a = a.mul(projectionMatrix);
            b = b.mul(projectionMatrix);

            //cutting
            if(!isInside(a) || !isInside(b))
                continue;

            //dehomogenization, W can be 0
            a = a.mul(1 / a.getW());
            b = b.mul(1 / b.getW());

            //transformation to window
            Vec3D vecA = transformToWindow(a);
            Vec3D vecB = transformToWindow(b);

            lineRasterizer.rasterize((int)Math.round(vecA.getX()), (int)Math.round(vecA.getY()), (int)Math.round(vecB.getX()), (int)Math.round(vecB.getY()), solid.getColor());
        }
    }
    public void renderSolid(SolidPart solid, Solid parent)
    {
        solid.createAlts();
        var ib = solid.altIndexBuffer;
        var vb = solid.altVertexBuffer;
        for (int i = 0; i < solid.altIndexBuffer.size(); i += 2) {
            if(vb.isEmpty())
            {
                vb = solid.getVertexBuffer();
            }
            int indexA = ib.get(i);
            int indexB = ib.get(i + 1);
            Point3D a = vb.get(indexA);
            Point3D b = vb.get(indexB);

            //transformations, MVP
            a = a.mul(parent.getModelMatrix());
            b = b.mul(parent.getModelMatrix());

            a = a.mul(viewMatrix);
            b = b.mul(viewMatrix);

            a = a.mul(projectionMatrix);
            b = b.mul(projectionMatrix);

            //cutting
            if(!isInside(a) || !isInside(b))
                continue;

            //dehomogenization, W can be 0
            a = a.mul(1 / a.getW());
            b = b.mul(1 / b.getW());

            //transformation to window
            Vec3D vecA = transformToWindow(a);
            Vec3D vecB = transformToWindow(b);

            lineRasterizer.rasterize((int)Math.round(vecA.getX()), (int)Math.round(vecA.getY()), (int)Math.round(vecB.getX()), (int)Math.round(vecB.getY()), solid.getColor());
        }
    }

    private Vec3D transformToWindow(Point3D p) {
        return new Vec3D(p).mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((double) (width - 1) / 2, (double) (height - 1) / 2,1));
    }

    private boolean isInside(Point3D p)
    {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        double w = p.getW();
        return x > -w && x < w && y > -w && y < w && z > -w && z < w;
    }

    public void renderSolids(List<Solid> solids)
    {
        for (Solid solid : solids)
        {
            renderSolid(solid);
        }
    }
    public void setViewMatrix(Mat4 viewMatrix) {
        this.viewMatrix = viewMatrix;
    }
    public void setProjectionMatrix(Mat4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }
}
