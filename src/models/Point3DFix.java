package models;

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
    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }
    public Point3DFix normalize() {
        float len = length();
        if (len == 0) return new Point3DFix(0, 0, 0);
        return new Point3DFix(x / len, y / len, z / len);
    }
}
