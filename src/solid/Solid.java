package solid;

import models.Point3DFix;
import models.PointMinMax;
import transforms.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected List<Point3D> vertexBuffer = new ArrayList<Point3D>();
    protected List<Integer> indexBuffer = new ArrayList<Integer>();
    protected Mat4 modelMatrix = new Mat4Identity();
    public Point3DFix coreBottom = new Point3DFix();
    public Point3DFix coreTop = new Point3DFix();
    protected int color = 0xFFFFFF;
    protected int subdivideLine = 0;

    public int getSubdivideLine() {
        return subdivideLine;
    }

    public void setSubdivideLine(int subdivideLine) {
        this.subdivideLine = subdivideLine;
    }

    protected void addIndices(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public void moveModel(double x, double y, double z) {
        modelMatrix = modelMatrix.mul(new Mat4Transl(x, y, z));
    }

    public PointMinMax getMaxAndMin() {
        Point3D first = vertexBuffer.get(0);

        Point3DFix min = new Point3DFix(first.getX(), first.getY(), first.getZ());
        Point3DFix max = new Point3DFix(first.getX(), first.getY(), first.getZ());

        for (Point3D p : vertexBuffer) {
            if (p.getX() < min.x) min.x = p.getX();
            if (p.getX() > max.x) max.x = p.getX();

            if (p.getY() < min.y) min.y = p.getY();
            if (p.getY() > max.y) max.y = p.getY();

            if (p.getZ() < min.z) min.z = p.getZ();
            if (p.getZ() > max.z) max.z = p.getZ();
        }
        return new PointMinMax(min, max);
    }

    public PointMinMax transformCore() {
        PointMinMax result = new PointMinMax(coreBottom, coreTop);
        Point3D worse = new Point3D(coreBottom.x, coreBottom.y, coreBottom.z);
        worse = worse.mul(modelMatrix);
        Point3D worse2 = new Point3D(coreTop.x, coreTop.y, coreTop.z);
        worse2 = worse2.mul(modelMatrix);
        result.max = new Point3DFix(worse);
        result.min = new Point3DFix(worse2);
        return result;
    }

    public boolean isInside(Point3D p) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        double w = p.getW();
        return x > -w && x < w && y > -w && y < w && z > -w && z < w;
    }

    public void rotateModel(double degrees, double x, double y, double z) {
        Point3DFix origin = getMaxAndMin().getOrigin();
        Mat4 model = new Mat4Transl(-origin.x, -origin.y, -origin.z);
        Mat4 inbetween = model.mul(new Mat4Rot(Math.toRadians(degrees), new Vec3D(x, y, z)));
        Mat4 finalStep = inbetween.mul(new Mat4Transl(origin.x, origin.y, origin.z));
        modelMatrix = finalStep.mul(modelMatrix);
    }

    public void scaleModel(double amount, double x, double y, double z) {
        modelMatrix = modelMatrix.mul(new Mat4Scale(1 + x * amount, 1 + y * amount, 1 + z * amount));
    }

    public void setScaleModel(double amount, double x, double y, double z) {
        modelMatrix = modelMatrix.mul(new Mat4Scale(x * amount, y * amount, z * amount));
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public Point3DFix getPosition() {
        return new Point3DFix(
                modelMatrix.get(3, 0),
                modelMatrix.get(3, 1),
                modelMatrix.get(3, 2)
        );
    }

    public Mat4 returnInversedMatrix() {
        double r00 = modelMatrix.get(0, 0), r01 = modelMatrix.get(0, 1), r02 = modelMatrix.get(0, 2);
        double r10 = modelMatrix.get(1, 0), r11 = modelMatrix.get(1, 1), r12 = modelMatrix.get(1, 2);
        double r20 = modelMatrix.get(2, 0), r21 = modelMatrix.get(2, 1), r22 = modelMatrix.get(2, 2);

// Transposed rotation
        double tR00 = r00, tR01 = r10, tR02 = r20;
        double tR10 = r01, tR11 = r11, tR12 = r21;
        double tR20 = r02, tR21 = r12, tR22 = r22;

        double tx = modelMatrix.get(0, 3), ty = modelMatrix.get(1, 3), tz = modelMatrix.get(2, 3);

        double invTx = -(tR00 * tx + tR01 * ty + tR02 * tz);
        double invTy = -(tR10 * tx + tR11 * ty + tR12 * tz);
        double invTz = -(tR20 * tx + tR21 * ty + tR22 * tz);

        Point3D invRow0 = new Point3D(tR00, tR01, tR02, invTx);
        Point3D invRow1 = new Point3D(tR10, tR11, tR12, invTy);
        Point3D invRow2 = new Point3D(tR20, tR21, tR22, invTz);
        Point3D invRow3 = new Point3D(0, 0, 0, 1);

        return new Mat4(invRow0, invRow1, invRow2, invRow3);
    }

    public void setModelMatrix(Mat4 modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Mat4 getModelMatrix() {
        return modelMatrix;
    }

    public void addBoxEdges(int i) {
        // front
        addIndices(i + 0, i + 1);
        addIndices(i + 1, i + 2);
        addIndices(i + 2, i + 3);
        addIndices(i + 3, i + 0);

        // back
        addIndices(i + 4, i + 5);
        addIndices(i + 5, i + 6);
        addIndices(i + 6, i + 7);
        addIndices(i + 7, i + 4);

        // connect front to back
        addIndices(i + 0, i + 4);
        addIndices(i + 1, i + 5);
        addIndices(i + 2, i + 6);
        addIndices(i + 3, i + 7);
    }

    public void resetModel() {
        modelMatrix = new Mat4Identity();
    }

}
