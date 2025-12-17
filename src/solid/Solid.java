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
}
