package controller;


import clip.Clipper;
import fill.Filler;
import fill.ScanLineFiller;
import fill.SeedFiller;
import objects.Line;
import objects.Point;
import objects.Polygon;
import objects.Rectangle;
import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.MarkRasterizer;
import special.ColorGradient;
import special.LineAligner;
import view.Panel;
import view.Window;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static rasterize.PolygonRasterizer.drawPolygon;

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
    private boolean dragging = false;
    //polygons
    private ArrayList<Polygon> polygons = new ArrayList<>();
    private int indexActivePolygon = 0;
    private boolean draggingPolygon = false;
    //cutting polygons
    private Polygon cuttingPolygon = null;
    private Polygon polygonToBeCut = null;
    private boolean draggingCutting = false;
    private boolean whichDrag = false;
    //rectangles
    private ArrayList<Rectangle> rectangles = new ArrayList<>();
    private int indexActiveRectangle = 0;
    //mode
    private Mode mode = Mode.Line;
    private boolean antialiasing = false;
    private boolean shiftMode = false;
    private FillMode fillMode = FillMode.SeedFill;
    //for title changes
    private Window window; //why no events :(
    //fills
    private ArrayList<Filler> fillers;

    public Controller2D(Panel panel, Window window) {
        this.panel = panel;
        this.window = window;
        window.changeTitle(mode.toString());
        initListeners();
        panel.repaint();
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        fillers = new ArrayList<>();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON3 && mode != Mode.CuttingPolygon) {
                    if (fillMode == FillMode.SeedFill) {
                        fillers.add(new SeedFiller(panel.getRaster(), new Point(me.getX(), me.getY()), color, fillMode));
                    }
                }
                switch (mode) {
                    case Line:
                        if (me.getButton() == MouseEvent.BUTTON1) {
                            firstPoint = new Point(me.getX(), me.getY());
                            indexActiveLine++;
                            dragging = true;
                        }
                        break;
                    case Polygon:
                        if (me.getButton() == MouseEvent.BUTTON2) {
                            if (polygons.get(indexActivePolygon).getPointsSize() < 2 && polygons.size() > 1)
                                break;
                            Polygon polygon = new Polygon(color);
                            polygon.colorGradient = colorGradient;
                            polygons.add(polygon);
                            indexActivePolygon++;
                            polygons.get(indexActivePolygon).addPoint(new Point(me.getX(), me.getY()));
                            if (fillMode == FillMode.ScanLine) {
                                fillers.add(new ScanLineFiller(polygon, lineRasterizer, color, fillMode));
                            }
                        } else if (me.getButton() == MouseEvent.BUTTON1) {
                            draggingPolygon = true;
                            polygons.get(indexActivePolygon).addPoint(new Point(me.getX(), me.getY()));
                        }
                        break;
                    case CuttingPolygon:
                        if (me.getButton() == MouseEvent.BUTTON3) {
                            draggingCutting = true;
                            whichDrag = true;
                            if (polygonToBeCut == null) {
                                polygonToBeCut = new Polygon(0xff2b5c);
                            }
                            polygonToBeCut.addPoint(new Point(me.getX(), me.getY()));
                        } else if (me.getButton() == MouseEvent.BUTTON1) {
                            draggingCutting = true;
                            whichDrag = false;
                            if (cuttingPolygon == null) {
                                cuttingPolygon = new Polygon(0x19ffa5);
                            }
                            cuttingPolygon.addPoint(new Point(me.getX(), me.getY()));
                        } else {
                            cuttingPolygon = null;
                            polygonToBeCut = null;
                        }
                        break;
                    case Mode.Rectangle:
                        Rectangle rectangle = rectangles.get(indexActiveRectangle);
                        if(rectangle.getPointsSize() == 2)
                        {
                            Point H = new Point(me.getX(), me.getY());
                            rectangle.makeRectangle(H);
                            indexActiveRectangle++;
                            rectangles.add(new Rectangle(color));
                        }
                        else{
                            rectangle.addPoint(new Point(me.getX(), me.getY()));
                        }
                        break;
                }
                drawScene();
            }

            public void mouseReleased(MouseEvent me) {
                switch (mode) {
                    case Line:
                        if (me.getButton() == MouseEvent.BUTTON1) {
                            dragging = false;
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
                        break;
                    case Polygon:
                        if (draggingPolygon) {
                            draggingPolygon = false;
                            polygons.get(indexActivePolygon).changeLastPoint(new Point(me.getX(), me.getY()));
                        }
                        break;
                    case CuttingPolygon:
                        if(draggingCutting) {
                            draggingCutting = false;
                            if (whichDrag) {
                                polygonToBeCut.changeLastPoint(new Point(me.getX(), me.getY()));
                            } else {
                                cuttingPolygon.changeLastPoint(new Point(me.getX(), me.getY()));
                            }
                        }
                        break;
                }
                drawScene();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent me) {
                switch (mode) {
                    case Line:
                        if (dragging) {
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
                        break;
                    case Polygon:
                        if (draggingPolygon) {
                            polygons.get(indexActivePolygon).changeLastPoint(new Point(me.getX(), me.getY()));
                        }
                        break;
                    case CuttingPolygon:
                        if(draggingCutting)
                        {
                            if(whichDrag)
                            {
                                polygonToBeCut.changeLastPoint(new Point(me.getX(), me.getY()));
                            }
                            else{
                                cuttingPolygon.changeLastPoint(new Point(me.getX(), me.getY()));
                            }
                        }
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
                        if(fillMode == FillMode.ScanLine)
                        {
                            fillers.add(new ScanLineFiller(polygon, lineRasterizer, color, fillMode));
                        }
                        break;
                    case KeyEvent.VK_L:
                        mode = Mode.Line;
                        window.changeTitle(mode.toString());
                        break;
                    case KeyEvent.VK_R:
                        mode = Mode.Rectangle;
                        window.changeTitle(mode.toString());
                        if(rectangles.isEmpty())
                        {
                            rectangles.add(new Rectangle(color));
                        }
                        break;
                    case KeyEvent.VK_O:
                        mode = Mode.CuttingPolygon;
                        window.changeTitle(mode.toString());
                    case KeyEvent.VK_I:
                        if(cuttingPolygon != null && mode == Mode.CuttingPolygon && cuttingPolygon.getPointsSize() >= 3)
                        {
                            cuttingPolygon.reversePoints();
                            drawScene();
                        }
                        break;
                    case KeyEvent.VK_A:
                        antialiasing = !antialiasing;
                        lineRasterizer = antialiasing ? new FilledLineRasterizer(panel.getRaster()) : new LineRasterizerTrivial(panel.getRaster());
                        drawScene();
                        break;
                    case KeyEvent.VK_SHIFT:
                        if (mode != Mode.Polygon) {
                            if (indexActiveLine == -1 || indexActiveLine == lines.size())
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
                        changeActiveColor();
                        break;
                    case KeyEvent.VK_DOWN:
                        colorGradient = window.selectColor();
                        color = -2;
                        changeActiveColor();
                        break;
                    case KeyEvent.VK_M:
                        fillMode = FillMode.ScanLine;
                        Polygon activePoly = polygons.get(indexActivePolygon);
                        if(!polygons.isEmpty() && activePoly.getPointsSize() > 0) {
                            fillers.add(new ScanLineFiller(activePoly, lineRasterizer, color, fillMode));
                        }
                        if (mode == Mode.Line) {
                            window.changeTitle("ScanLine needs a new polygon.... Also, easter egg!");
                        } else {
                            window.changeTitle(mode.toString() + " " + fillMode.toString());
                        }
                        drawScene();
                        break;
                    case KeyEvent.VK_N:
                        fillMode = FillMode.SeedFill;
                        window.changeTitle(mode.toString() + " " + fillMode.toString());
                        drawScene();
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

    private void shiftAlign(int x, int y, Line activeLine) {
        var alignedLine = LineAligner.alignLine(x, y, activeLine);
        activeLine.End = alignedLine.End;
    }

    private void clearScene() {
        polygons.clear();
        lines.clear();
        fillers.clear();
        indexActiveLine = -1;
        firstPoint = null;
        indexActivePolygon = 0;
        cuttingPolygon = null;
        polygonToBeCut = null;
        rectangles.clear();
        panel.getRaster().clear();
        if (mode == Mode.Polygon) {
            Polygon polygon = new Polygon(color);
            polygons.add(polygon);
        }
    }



    private void drawScene() {
        panel.getRaster().clear();
        for (Polygon polygon : polygons) {
            drawPolygon(polygon, lineRasterizer);
        }
        for (Line line : lines) {
            lineRasterizer.setColorGradient(line.colorGradient);
            lineRasterizer.rasterize(line.Start.X, line.Start.Y, line.End.X, line.End.Y, line.Color);
        }
        for (Filler filler : fillers)
        {
            if (filler.getFillMode() != fillMode)
                continue;
            filler.fill();
        }
        for (Rectangle rectangle : rectangles)
        {
            drawPolygon(rectangle, lineRasterizer);
        }
        if (polygonToBeCut != null && cuttingPolygon != null && polygonToBeCut.getPointsSize() >= 3 && cuttingPolygon.getPointsSize() >= 3) {
            Clipper clipper = new Clipper();
            ArrayList<Point> clippedPoints = clipper.clip(cuttingPolygon, polygonToBeCut);
            if (!clippedPoints.isEmpty()) {
                Filler scanLine = new ScanLineFiller(new Polygon(clippedPoints), lineRasterizer, 0xff6900, FillMode.ScanLine);
                scanLine.fill();
            }
        }
        drawPolygon(cuttingPolygon, lineRasterizer);
        drawPolygon(polygonToBeCut, lineRasterizer);
        panel.repaint();
    }
}
