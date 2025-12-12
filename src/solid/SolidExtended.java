package solid;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class SolidExtended extends Solid{
    protected ArrayList<SolidPart> parts = new ArrayList<>();
    public void createTotal()
    {
        int last = 0;
        for (SolidPart part : parts) {
            vertexBuffer.addAll(part.vertexBuffer);
            for (int j = 0; j < part.indexBuffer.size(); j++) {
                indexBuffer.add(part.indexBuffer.get(j) + last);
            }
            last = getLargest(part.indexBuffer) + 1;
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
}
