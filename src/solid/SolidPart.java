package solid;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class SolidPart extends Solid {
    private int nextIndex;
    public String type = "Basic";
    public double width;
    public double height;
    public double depth;
    public List<Point3D> altVertexBuffer = new ArrayList<>();
    public List<Integer> altIndexBuffer = new ArrayList<>();

    public void createAlts() {
        if(subdivideLine == 0)
        {
            altVertexBuffer.addAll(vertexBuffer);
            altIndexBuffer.addAll(indexBuffer);
        }
        ArrayList<Integer> fakeList = new ArrayList<>();
        altVertexBuffer.clear();
        altIndexBuffer.clear();

        altVertexBuffer.addAll(vertexBuffer);

        nextIndex = altVertexBuffer.size();

        for (int i = 0; i < indexBuffer.size(); i += 2) {
            int index1 = indexBuffer.get(i);
            int index2 = indexBuffer.get(i + 1);
            subdivideEdge(fakeList,index1, index2,subdivideLine);
        }

        altIndexBuffer.addAll(fakeList);
    }

    public void subdivideEdge(
            ArrayList<Integer> outIndices,
            int index1,
            int index2,
            int subdivisions
    ) {
        Point3D p1 = altVertexBuffer.get(index1);
        Point3D p2 = altVertexBuffer.get(index2);

        int prev = index1;

        for (int i = 1; i <= subdivisions; i++) {
            double t = (double) i / (subdivisions + 1);

            Point3D p = new Point3D(
                    p1.getX() + (p2.getX() - p1.getX()) * t,
                    p1.getY() + (p2.getY() - p1.getY()) * t,
                    p1.getZ() + (p2.getZ() - p1.getZ()) * t
            );

            int newIndex = altVertexBuffer.size();
            altVertexBuffer.add(p);

            outIndices.add(prev);
            outIndices.add(newIndex);

            prev = newIndex;
        }

        outIndices.add(prev);
        outIndices.add(index2);
    }

    //old
    public void makeMidPoint(ArrayList<Integer> list, int index1, int index2, int subdivisionCount)
    {
        list.add(index1);
        list.add(nextIndex);
        list.add(nextIndex);
        list.add(index2);
        createMidpoint(index1, index2);
        nextIndex ++;
    }
    private void createMidpoint(int i1, int i2) {
        Point3D p1 = vertexBuffer.get(i1);
        Point3D p2 = vertexBuffer.get(i2);

        Point3D mid = new Point3D(
                (p1.getX() + p2.getX()) * 0.5,
                (p1.getY() + p2.getY()) * 0.5,
                (p1.getZ() + p2.getZ()) * 0.5
        );

        altVertexBuffer.add(mid);
    }
}
