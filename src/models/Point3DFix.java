package models;

import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

public class Point3DFix {
    public double x;
    public double y;
    public double z;
    public double w;
    public Point3DFix(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    public Point3DFix(Vec3D vec) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }
    public Point3DFix(Point3D vec) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }
    public Point3DFix(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Point3DFix() {
        x = 0;
        y = 0;
        z = 0;
    }
    public Point3DFix add(final Point3DFix p) {
        return new Point3DFix(x + p.x, y + p.y, z + p.z, w + p.w);
    }
    public Point3DFix sub(final Point3DFix p) {
        return new Point3DFix(x - p.x, y - p.y, z - p.z, w - p.w);
    }
    public Point3DFix mul(double scalar) {
        return new Point3DFix(x * scalar, y * scalar, z * scalar);
    }
    public Point3DFix mul(Point3DFix p) {
        return new Point3DFix(x * p.x, y * p.y, z * p.z, w * p.w);
    }
    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }
    public Point3DFix normalize() {
        float len = length();
        if (len == 0) return new Point3DFix(0, 0, 0);
        return new Point3DFix(x / len, y / len, z / len);
    }
    public Point3DFix mulPoint(Mat4 m) {
        double nx =
                m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z + m.get(0,3);
        double ny =
                m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z + m.get(1,3);
        double nz =
                m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z + m.get(2,3);
        double nw =
                m.get(3,0) * x + m.get(3,1) * y + m.get(3,2) * z + m.get(3,3);

        if (nw != 0.0 && nw != 1.0) {
            nx /= nw;
            ny /= nw;
            nz /= nw;
        }

        return new Point3DFix(nx, ny, nz);
    }
    public Point3DFix mulVector(Mat4 m) {
        double nx =
                m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z;
        double ny =
                m.get(1,0) * x + m.get(1,1) * y + m.get(1,2)* z;
        double nz =
                m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z;

        return new Point3DFix(nx, ny, nz);
    }
    public static double dot(Point3DFix p1, Point3DFix p2)
    {
        return (p1.x * p2.x) + (p1.y * p2.y) + (p1.z * p2.z);
    }
}
