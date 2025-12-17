package solid;

import models.Point3DFix;
import transforms.Mat4;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class SolidExtended extends Solid{
    public ArrayList<SolidPart> parts = new ArrayList<>();
    private int globalColor;
    public void createTotal()
    {
        int last = 0;
        for (SolidPart part : parts) {
            vertexBuffer.addAll(part.vertexBuffer);
            for (int j = 0; j < part.indexBuffer.size(); j++) {
                indexBuffer.add(part.indexBuffer.get(j) + last);
            }
            last += getLargest(part.indexBuffer) + 1;
        }
    }
    private int getLargest(List<Integer> list)
    {
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) > max)
            {
                max = list.get(i);
            }
        }
        return max;
    }
    public void setAllColor(int color)
    {
        globalColor = color;
        for (SolidPart part : parts) {
            part.setColor(color);
        }
    }
    public int getGlobalColor()
    {
        return globalColor;
    }
    public ArrayList<Point3DFix> transformPoints()
    {
        ArrayList<Point3DFix> points = new ArrayList<>();
        SolidPart mainPart = parts.get(0);
        mainPart.createAlts();
        for (int i = 0; i < mainPart.altIndexBuffer.size(); i++) {
            Point3D point = mainPart.altVertexBuffer.get(mainPart.altIndexBuffer.get(i));
            Point3DFix pointFix = new Point3DFix(point.mul(modelMatrix));
            points.add(pointFix);
        }
        return points;
    }
}
