package solid;

import transforms.Point3D;

public class Floor extends SolidExtended{
    public Floor() {
        subdivideLine = 2;
        setColor(0x7e503b);
        int brickAmount = 50;
        double offsetX = 0.23;
        double offsetY = 0.13;
        double offsetEntireX = (double) -brickAmount / 2 * 0.1 - 0.3;
        double offsetEntireY = -2;
        for (int i = 0; i < brickAmount / 2; i++) {
            for (int j = 0; j < brickAmount; j++) {
                if(j % 2 == 0)
                {
                    parts.add(makeBrick(i * offsetX + 0.1,j * offsetY, offsetEntireX, offsetEntireY));
                }
                else{
                    parts.add(makeBrick(i * offsetX,j * offsetY, offsetEntireX, offsetEntireY));
                }
            }
        }
    }
    private SolidPart makeBrick(double xOffset, double yOffset, double brickOffsetX, double brickOffsetY)
    {
        SolidPart brick = new SolidPart();
        brick.setColor(0x7e503b);
        double height = 0.025;
        double width = 0.1;
        double depth = 0.04;
        brick.vertexBuffer.add(new Point3D(-width + xOffset + brickOffsetX,-depth + yOffset + brickOffsetY,-height));
        brick.vertexBuffer.add(new Point3D(width + xOffset + brickOffsetX,-depth + yOffset + brickOffsetY,-height));
        brick.vertexBuffer.add(new Point3D(-width + xOffset + brickOffsetX,depth + yOffset + brickOffsetY,-height));
        brick.vertexBuffer.add(new Point3D(-width + xOffset + brickOffsetX,-depth + yOffset + brickOffsetY,height));
        brick.vertexBuffer.add(new Point3D(width + xOffset + brickOffsetX,depth + yOffset + brickOffsetY,-height));
        brick.vertexBuffer.add(new Point3D(-width + xOffset + brickOffsetX,depth + yOffset + brickOffsetY,height));
        brick.vertexBuffer.add(new Point3D(width + xOffset + brickOffsetX,-depth + yOffset + brickOffsetY,height));
        brick.vertexBuffer.add(new Point3D(width + xOffset + brickOffsetX,depth + yOffset + brickOffsetY,height));

        brick.addIndices(0, 1);
        brick.addIndices(0, 2);
        brick.addIndices(0, 3);

        brick.addIndices(1, 4);
        brick.addIndices(1, 6);

        brick.addIndices(2, 4);
        brick.addIndices(2, 5);

        brick.addIndices(3, 6);
        brick.addIndices(3, 5);

        brick.addIndices(4, 7);
        brick.addIndices(5, 7);
        brick.addIndices(6, 7);
        return brick;
    }
}
