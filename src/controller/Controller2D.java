package controller;


import objects.Line;
import objects.Point;
import objects.Polygon;
import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import special.ColorGradient;
import view.Panel;
import view.Window;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller2D {
    //base
    private final Panel panel;
    //rasterizers
    private LineRasterizer lineRasterizer;
    //color
    private int color = 0xFFFFFF;
    private final ArrayList<Integer> colors = new ArrayList<Integer>(Arrays.asList(0xFFFFFF, 0xFF00FF, 0xFFFF00, 0x00FFFF, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFA000, 0x562B00, 0xAAAAAA, 0x000000));
    private int colorIndex = 0;
    private ColorGradient colorGradient = null;
    //lines
    private ArrayList<Line> lines = new ArrayList<Line>();
    private Point firstPoint;
    private int indexActiveLine = -1;
    //polygons
    private ArrayList<Polygon> polygons = new ArrayList<>();
    private int indexActivePolygon = 0;
    //mode
    private Mode mode = Mode.Line;
    private boolean antialiasing = false;
    private boolean shiftMode = false;
    //for title changes
    private Window window; //why no events :(

    public Controller2D(Panel panel, Window window) {
        this.panel = panel;
        this.window = window;
        window.changeTitle(mode.toString());
        initListeners();
        panel.repaint();
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                switch (mode) {
                    case Line:
                        firstPoint = new Point(me.getX(), me.getY());
                        indexActiveLine++;
                        break;
                    case Polygon:
                        if (me.getButton() == MouseEvent.BUTTON3) {
                            if (polygons.get(indexActivePolygon).getPointsSize() < 2 && polygons.size() > 1)
                                break;
                            Polygon polygon = new Polygon(color);
                            polygon.colorGradient = colorGradient;
                            polygons.add(polygon);
                            indexActivePolygon++;
                            polygons.get(indexActivePolygon).addPoint(new Point(me.getX(), me.getY()));
                        } else {
                            polygons.get(indexActivePolygon).addPoint(new Point(me.getX(), me.getY()));
                        }
                        break;
                }
                drawScene();
            }

            public void mouseReleased(MouseEvent me) {
                switch (mode) {
                    case Line:
                        if (indexActiveLine == -1)
                            return;
                        if (shiftMode)
                            break;
                        if (indexActiveLine == lines.size()) {
                            lines.add(new Line(firstPoint, new Point(me.getX(), me.getY()), color));
                        }
                        var activeLine = lines.get(indexActiveLine);
                        activeLine.End = new Point(me.getX(), me.getY());
                        break;
                }
                drawScene();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent me) {
                switch (mode) {
                    case Line:
                        if (indexActiveLine == -1) {
                            return;
                        }
                        if (indexActiveLine == lines.size()) {
                            lines.add(new Line(firstPoint, new Point(me.getX(), me.getY()), color));
                        }
                        var activeLine = lines.get(indexActiveLine);
                        activeLine.colorGradient = colorGradient;
                        activeLine.Color = color;
                        if (shiftMode) {
                            shiftAlign(me.getX(), me.getY(), activeLine);
                            break;
                        }
                        activeLine.End = new Point(me.getX(), me.getY());
                        break;
                }
                drawScene();
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftMode = false;
                    window.changeTitle(mode.toString());
                }
            }

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_P:
                        mode = Mode.Polygon;
                        window.changeTitle(mode.toString());
                        Polygon polygon = new Polygon(color);
                        polygons.add(polygon);
                        break;
                    case KeyEvent.VK_L:
                        mode = Mode.Line;
                        window.changeTitle(mode.toString());
                        break;
                    case KeyEvent.VK_A:
                        antialiasing = !antialiasing;
                        lineRasterizer = antialiasing ? new FilledLineRasterizer(panel.getRaster()) : new LineRasterizerTrivial(panel.getRaster());
                        drawScene();
                        break;
                    case KeyEvent.VK_SHIFT:
                        if (mode != Mode.Polygon) {
                            if(indexActiveLine == -1 || indexActiveLine == lines.size())
                                return;
                            shiftMode = true;
                            var activeLine = lines.get(indexActiveLine);
                            shiftAlign(activeLine.End.X, activeLine.End.Y, activeLine);
                            window.changeTitle("Line (Shift)");
                            drawScene();
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        colorIndex++;
                        if (colorIndex >= colors.size()) {
                            colorIndex = 0;
                        }
                        color = colors.get(colorIndex);
                        colorGradient = null;
                        changeActiveColor();
                        break;
                    case KeyEvent.VK_LEFT:
                        colorIndex--;
                        if (colorIndex < 0) {
                            colorIndex = colors.size() - 1;
                        }
                        color = colors.get(colorIndex);
                        colorGradient = null;
                        changeActiveColor();
                        break;
                    case KeyEvent.VK_UP:
                        color = -1;
                        colorGradient = null;
                        changeActiveColor(); //TODO: rainbow first thing fucks up
                        break;
                    case KeyEvent.VK_DOWN:
                        colorGradient = window.selectColor();
                        color = -2;
                        changeActiveColor();
                        break;
                    case KeyEvent.VK_C:
                        clearScene();
                        panel.repaint();
                        colorGradient = null;
                        break;
                }
            }
        });
    }

    private void changeActiveColor() {
        switch (mode) {
            case Line:
                Line activeLine;
                if (indexActiveLine < lines.size() && indexActiveLine >= 0) {
                    activeLine = lines.get(indexActiveLine);
                } else if (indexActiveLine > lines.size()) {
                    activeLine = lines.get(indexActiveLine - 1);
                } else {
                    return;
                }
                activeLine.colorGradient = colorGradient;
                activeLine.Color = color;
                break;
            case Polygon:
                Polygon activePolygon;
                if (indexActivePolygon < polygons.size()) {
                    activePolygon = polygons.get(indexActivePolygon);
                } else if (indexActivePolygon > polygons.size()) {
                    activePolygon = polygons.get(indexActivePolygon - 1);
                } else {
                    return;
                }
                activePolygon.colorGradient = colorGradient;
                activePolygon.Color = color;
                break;
        }
        drawScene();
    }

    private static void shiftAlign(int x, int y, Line activeLine) {
        boolean pointingLeft = activeLine.Start.X > x;
        float k = (float) (y - activeLine.Start.Y) / (x - activeLine.Start.X);
        float length = (float) Math.sqrt(Math.pow((x - activeLine.Start.X), 2) + Math.pow((y - activeLine.Start.Y), 2));
        var fortyFiveDegreeVector = length * (1 / (Math.sqrt(1 + Math.pow(1, 2))));
        var fortyFiveDegreeVectorMinus = length * (1 / (Math.sqrt(1 + Math.pow(-1, 2))));
        var fortyFiveDegreeVectorMinusVersionTwo = length * (-1 / (Math.sqrt(1 + Math.pow(-1, 2))));
        if (pointingLeft && k >= 0.5 && k < 2.5) // left, 45deg
        {
            int x2 = (int) (activeLine.Start.X - fortyFiveDegreeVector);
            int y2 = (int) (activeLine.Start.Y - fortyFiveDegreeVector);
            activeLine.End = new Point(x2, y2);
        } else if (pointingLeft && k <= -0.5 && k > -2.5) //left, -45deg
        {
            int x2 = (int) (activeLine.Start.X - fortyFiveDegreeVectorMinus);
            int y2 = (int) (activeLine.Start.Y - fortyFiveDegreeVectorMinusVersionTwo);
            activeLine.End = new Point(x2, y2);
        } else if (!pointingLeft && k >= 0.5 && k < 2.5) //right, 45deg
        {
            int x2 = (int) (activeLine.Start.X + fortyFiveDegreeVector);
            int y2 = (int) (activeLine.Start.Y + fortyFiveDegreeVector);
            activeLine.End = new Point(x2, y2);
        } else if (!pointingLeft && k <= -0.5 && k > -2.5) //right, -45deg
        {
            int x2 = (int) (activeLine.Start.X + fortyFiveDegreeVectorMinus);
            int y2 = (int) (activeLine.Start.Y + fortyFiveDegreeVectorMinusVersionTwo);
            activeLine.End = new Point(x2, y2);
        } else if (k < 0.5 && k > -0.5) // left and right, 0deg
        {
            activeLine.End = new Point(x, activeLine.Start.Y);
        } else // top and down, 90deg
        {
            activeLine.End = new Point(activeLine.Start.X, y);
        }
    }

    private void clearScene() {
        polygons.clear();
        lines.clear();
        indexActiveLine = -1;
        firstPoint = null;
        indexActivePolygon = 0;
        panel.getRaster().clear();
        if (mode == Mode.Polygon) {
            Polygon polygon = new Polygon(color);
            polygons.add(polygon);
        }
    }

    private void drawScene() {
        panel.getRaster().clear();
        for (Polygon polygon : polygons) {
            int size = polygon.getPointsSize();
            if (size < 3) {
                for (int i = 0; i < size; i++) {
                    var middlePoint = polygon.getPoint(i);
                    lineRasterizer.rasterize(middlePoint.X - 3, middlePoint.Y - 3, middlePoint.X + 3, middlePoint.Y + 3, 0xFF0000);
                    lineRasterizer.rasterize(middlePoint.X + 3, middlePoint.Y - 3, middlePoint.X - 3, middlePoint.Y + 3, 0xFF0000);
                }
                continue;
            }
            for (int i = 0; i < size; i++) {
                lineRasterizer.setColorGradient(polygon.colorGradient);
                if (i + 1 >= size) {
                    lineRasterizer.rasterize(polygon.getPoint(i), polygon.getPoint(0), polygon.Color);
                } else {
                    lineRasterizer.rasterize(polygon.getPoint(i), polygon.getPoint(i + 1), polygon.Color);
                }
            }
        }
        for (Line line : lines) {
            lineRasterizer.setColorGradient(line.colorGradient);
            lineRasterizer.rasterize(line.Start.X, line.Start.Y, line.End.X, line.End.Y, line.Color);
        }
        panel.repaint();
    }
}
