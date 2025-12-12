package solid;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolidPart {
    protected List<Point3D> vertexBuffer = new ArrayList<Point3D>();
    protected List<Integer> indexBuffer = new ArrayList<Integer>();
    protected int color = 0xFFFFFF;
    public void addIndices(Integer... indices){
        indexBuffer.addAll(Arrays.asList(indices));
    }
}
