package controller;

import models.Point3DFix;
import models.PointMinMax;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.TextRasterize;
import render.Renderer;
import solid.*;
import transforms.*;
import view.Panel;
import view.Window;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class Controller3D {
    //base
    private final Panel panel;
    private Window window;

    //rasterizers
    private LineRasterizer lineRasterizer;
    private TextRasterize textRasterizer;
    //renderers
    private Renderer renderer;
    //solids
    private ArrayList<Solid> solids = new ArrayList<>();
    private SolidExtended ray = null;
    private Solid activeSolid;
    private int activeSolidIndex;
    private Curve curve;
    private CurveType curveType = CurveType.Ferguson;
    private SolidExtended gun;
    private SolidExtended gunShootFX;
    private SolidExtended floor;
    private boolean shooting = false;
    private double distance = 1.7;
    private Dummy dummy;
    private ArrayList<Dummy> dummies = new ArrayList<>();
    private ArrayList<Solid> axes = new ArrayList<Solid>();
    //camera
    private Camera camera;
    private boolean allowFreeMovement = false;
    private Vec3D gunOffsetRot = new Vec3D(0, 1, -0.3);
    //matrices
    private Mat4 projectionMatrix;
    private Mat4 alterativeProjectionMatrix;
    private boolean isAltActive = false;
    //rotation
    private Point3DFix rotationAxis = new Point3DFix(1, 0, 0);
    private Point3DFix scaleAxis = new Point3DFix(1, 0, 0);
    //jump
    private boolean jumping = false;
    //movement
    private final boolean[] keys = new boolean[256];
    private double SPEED = 0.5 / 60 * 2;
    private Robot robot;
    private boolean recentering = false;
    private boolean escaped = false;
    //text stuff
    private int hitCount = 0;
    private double time = 0;
    private boolean counting = false;

    public Controller3D(Panel panel, Window window) {
        this.panel = panel;
        this.window = window;
        try {
            this.robot = new Robot();
        } catch (AWTException ignore) {
        }
        this.camera = new Camera().withPosition(new Vec3D(0.5, -1.5, 1))
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(true);
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        textRasterizer = new TextRasterize(panel.getRaster());
        this.projectionMatrix = new Mat4PerspRH(Math.toRadians(90)
                , (double) panel.getRaster().getHeight() / panel.getRaster().getWidth(),
                0.1,
                100);
        this.alterativeProjectionMatrix = new Mat4OrthoRH(2, 2, 0.1, 100);
        renderer = new Renderer(lineRasterizer, panel.getRaster().getWidth(), panel.getRaster().getHeight(),
                camera.getViewMatrix(),
                projectionMatrix);
        initListeners();

        //solids
        solids.add(new Arrow());
        activeSolidIndex = 0;
        activeSolid = solids.getFirst();
        solids.add(new Cube());
        solids.get(1).moveModel(-1,0,0.5);
        curve = new Curve();
        solids.add(curve);


        //gun stuff
        gun = new Gun();
        gunShootFX = new GunShoot();
        floor = new Floor();
        setupDummies();

        //axes
        axes.add(new Axis(Direction.X));
        axes.add(new Axis(Direction.Y));
        axes.add(new Axis(Direction.Z));
        drawScene();
        //main loop
        new Thread(() -> Update(60)).start();
    }
    private void setupDummies()
    {
        dummy = new Dummy();
        dummy.moveModel(0,10,1.6);
        dummy.setScaleModel(0.35, 1,1,1);
        dummy.rotateModel(90,1,0,0);
        dummies.add(dummy);
        dummy = new Dummy();
        dummy.moveModel(10,10,1.6);
        dummy.setScaleModel(0.35, 1,1,1);
        dummy.rotateModel(90,1,0,0);
        dummy.rotateModel(135,0,1,0);
        dummies.add(dummy);
        dummy = new Dummy();
        dummy.moveModel(-10,10,1.6);
        dummy.setScaleModel(0.35, 1,1,1);
        dummy.rotateModel(90,1,0,0);
        dummy.rotateModel(45,0,1,0);
        dummies.add(dummy);
        dummy = new Dummy();
        dummy.moveModel(0,-10,1.6);
        dummy.setScaleModel(0.35, 1,1,1);
        dummy.rotateModel(90,1,0,0);
        dummies.add(dummy);
        dummy = new Dummy();
        dummy.moveModel(0,0,-5);
        dummy.setScaleModel(0.35, 1,1,1);
        dummies.add(dummy);
        dummy = new Dummy();
        dummy.moveModel(0,0,10);
        dummy.setScaleModel(0.35, 1,1,1);
        dummies.add(dummy);
    }
    private void handleMouseMove(int mouseX, int mouseY)
    {
        if (escaped)
            return;
        if (recentering) {
            recentering = false;
            return;
        }

        int dx = mouseX - panel.getWidth() / 2;
        int dy = mouseY - panel.getHeight() / 2;

        double sensitivity = 0.002;
        camera = camera.addAzimuth(-dx * sensitivity)
                .addZenith(-dy * sensitivity);

        recentering = true;
        robot.mouseMove(
                panel.getLocationOnScreen().x + panel.getWidth() / 2,
                panel.getLocationOnScreen().y + panel.getHeight() / 2
        );

        double angle = camera.getAzimuth();
        double dxd = Math.cos(angle) * distance;
        double dyd = Math.sin(angle) * distance;
        gunOffsetRot = new Vec3D(dxd, dyd, -0.3);
    }
    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(escaped)
                    return;
                new Thread(() -> shoot(20)).start();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent me) {
                handleMouseMove(me.getX(), me.getY());
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e.getX(), e.getY());
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //toggles
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        escaped = !escaped;
                        break;
                    case KeyEvent.VK_F:
                        allowFreeMovement = !allowFreeMovement;
                    case KeyEvent.VK_Z:
                        rotationAxis.z = rotationAxis.z == 1 ? 0 : 1;
                        scaleAxis.z = scaleAxis.z == 1 ? 0 : 1;
                        break;
                    case KeyEvent.VK_Y:
                        rotationAxis.y = rotationAxis.y == 1 ? 0 : 1;
                        scaleAxis.y = scaleAxis.y == 1 ? 0 : 1;
                        break;
                    case KeyEvent.VK_X:
                        rotationAxis.x = rotationAxis.x == 1 ? 0 : 1;
                        scaleAxis.x = scaleAxis.x == 1 ? 0 : 1;
                        break;
                    case KeyEvent.VK_C:
                        switch (curveType){
                            case Ferguson -> curveType = CurveType.Coons;
                            case Coons -> curveType = CurveType.Bezier;
                            case Bezier -> curveType = CurveType.Ferguson;
                        }
                        curve.recalculate(curveType);
                        break;
                    case KeyEvent.VK_M:
                        activeSolidIndex++;
                        if (activeSolidIndex == solids.size())
                            activeSolidIndex = 0;
                        activeSolid = solids.get(activeSolidIndex);
                        break;
                    case KeyEvent.VK_N:
                        activeSolidIndex--;
                        if (activeSolidIndex < 0)
                            activeSolidIndex = solids.size() - 1;
                        activeSolid = solids.get(activeSolidIndex);
                        break;
                    case KeyEvent.VK_P:
                        if (!isAltActive) {
                            renderer.setProjectionMatrix(alterativeProjectionMatrix);
                            isAltActive = true;
                        } else {
                            renderer.setProjectionMatrix(projectionMatrix);
                            isAltActive = false;
                        }
                        break;
                }
                if (escaped)
                    return;
                //updates
                int code = e.getKeyCode();
                if (code < 256) keys[code] = true;
            }

            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code < 256) keys[code] = false;
            }
        });
    }
    private void shoot(int wait)
    {
        shooting = true;

        double yaw   = camera.getAzimuth();
        double pitch = camera.getZenith();
        Point3DFix rayOrigin = new Point3DFix(camera.getPosition());
        Point3DFix rayDir = new Point3DFix(Math.cos(yaw), Math.sin(yaw), pitch).normalize();

        Point3DFix rayStart = rayOrigin;
        Point3DFix rayEnd = rayOrigin.add(rayDir.mul(10.0));
        ray = new Ray(rayStart, rayEnd);
        ArrayList<Point3DFix> transformedPoints = ray.transformPoints();
        for (Dummy dummyE : dummies) {
            PointMinMax core = dummyE.transformCore();
            for (Point3DFix point : transformedPoints) {
                double distanceBottom = getDistance(point, core.min);
                double distanceTop = getDistance(point, core.max);
                if(distanceBottom <= 0.5 || distanceTop <= 0.5)
                {
                    if(!dummyE.isHit) {
                        dummyE.isHit = true;
                        hitCount++;
                        checkHits();
                    }
                    new Thread(() -> flash(dummyE, 0xff0000, 0.1)).start();
                    break;
                }
            }
        }
        try {
            Thread.sleep(wait);
        } catch (InterruptedException ignored) {}
        shooting = false;
    }
    private void checkHits()
    {
        if(hitCount >= dummies.size())
        {
            counting = false;
        }
    }
    private void startCounting()
    {
        counting = true;
        time = 0;
        while(counting)
        {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ignored) {}
            if(escaped)
                continue;
            time += 0.1;
        }
    }
    private double getDistance(Point3DFix p1, Point3DFix p2) {
        return Math.sqrt(Math.pow(p2.x-p1.x ,2)+Math.pow(p2.y-p1.y ,2)+Math.pow(p2.z-p1.z ,2));
    }
    private void Update(int fps) {
        new Thread(this::startCounting).start();
        while (true) {
            double speedBoost = 1;
            if(keys[KeyEvent.VK_SHIFT])
            {
                speedBoost = 2;
            }
            if (keys[KeyEvent.VK_W]) {
                camera = camera.forward(SPEED * speedBoost);
            }
            if (keys[KeyEvent.VK_S]) {
                camera = camera.backward(SPEED * speedBoost);
            }
            if (keys[KeyEvent.VK_A]) {
                camera = camera.left(SPEED * speedBoost);
            }
            if (keys[KeyEvent.VK_D]) {
                camera = camera.right(SPEED * speedBoost);
            }
            Mat4 model = new Mat4Identity();
            model = model.mul(new Mat4Scale(0.8, 0.8, 0.8));
            model = model.mul(new Mat4Transl(camera.getPosition())).mul(new Mat4Transl(gunOffsetRot));
            model = rotateMat4(camera.getAzimuth() * 180 / Math.PI, 0, 0, 1).mul(model);
            model = rotateMat4(90, 1, 0, 0).mul(model);
            gunShootFX.setModelMatrix(model);
            gun.setModelMatrix(model);
            if (!allowFreeMovement) {
                Vec3D cameraPos = camera.getPosition();
                camera = camera.withPosition(new Vec3D(cameraPos.getX(), cameraPos.getY(), 1));
            }
            if (keys[KeyEvent.VK_SPACE]) {
                if (!jumping)
                    new Thread(() -> jump(activeSolid, 0.5, 1)).start();
            }
            if (keys[KeyEvent.VK_LEFT]) activeSolid.moveModel(-SPEED * speedBoost, 0, 0);
            if (keys[KeyEvent.VK_RIGHT]) activeSolid.moveModel(SPEED * speedBoost, 0, 0);
            if (keys[KeyEvent.VK_UP]) activeSolid.moveModel(0, SPEED * speedBoost, 0);
            if (keys[KeyEvent.VK_DOWN]) activeSolid.moveModel(0, -SPEED * speedBoost, 0);
            if (keys[KeyEvent.VK_R]) activeSolid.rotateModel(5, rotationAxis.x, rotationAxis.y, rotationAxis.z);
            if (keys[KeyEvent.VK_T]) activeSolid.scaleModel(SPEED, scaleAxis.x, scaleAxis.y, scaleAxis.z);
            drawScene();
            try {
                Thread.sleep((long) 1000 / fps);
            } catch (Exception ignore) {
            }
        }
    }

    public Mat4 rotateMat4(double degrees, double x, double y, double z) {
        Point3DFix origin = gun.getMaxAndMin().getOrigin();
        Mat4 model = new Mat4Transl(-origin.x, -origin.y, -origin.z);
        Mat4 inbetween = model.mul(new Mat4Rot(Math.toRadians(degrees), new Vec3D(x, y, z)));
        Mat4 finalStep = inbetween.mul(new Mat4Transl(origin.x, origin.y, origin.z));
        return finalStep;
    }
    private void flash(SolidExtended target, int targetColor, double seconds)
    {
        int previousColor = target.getGlobalColor();
        target.setAllColor(targetColor);
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException ignored) {
        }
        target.setAllColor(previousColor);
    }
    private void jump(Solid target, double targetZ, int seconds) {
        jumping = true;
        int frames = seconds * 60;
        int halfway = frames / 2;
        double step = targetZ / halfway;

        long frameTime = (long) (1000.0 / 60.0);

        // UP
        for (int i = 0; i < halfway; i++) {
            target.moveModel(0, 0, step);
            try {
                Thread.sleep(frameTime);
            } catch (InterruptedException ignored) {
            }
        }

        // DOWN
        for (int i = 0; i < halfway; i++) {
            target.moveModel(0, 0, -step);
            try {
                Thread.sleep(frameTime);
            } catch (InterruptedException ignored) {
            }
        }
        jumping = false;
    }

    private void drawScene() {
        panel.getRaster().clear();
        if (escaped) {
            textRasterizer.rasterize(20, 20, "Paused");
        }
        renderer.setViewMatrix(camera.getViewMatrix());
        renderer.renderSolidExtended(floor);
        for (Solid axis : axes) {
            renderer.renderSolid(axis);
        }
        renderer.renderSolids(solids);
        if(ray != null)
        {
            renderer.renderSolidExtended(ray);
        }
        renderer.renderSolidExtended(gun);
        if(shooting) {
            renderer.renderSolidExtended(gunShootFX);
        }
        for (SolidExtended dummyExt : dummies) {
            renderer.renderSolidExtended(dummyExt);
        }

        textRasterizer.rasterize(20, panel.getHeight() - 40, "Press 'Esc' for free mouse movement.");
        textRasterizer.rasterize(20, panel.getHeight() - 20, "Press 'F' for free camera movement. " + (allowFreeMovement ? "(Enabled)" : "(Disabled)"));
        textRasterizer.rasterize(panel.getWidth() - 215, panel.getHeight() - 80, "Curve type: " + curveType.name() + " ('C' to change.)");
        textRasterizer.rasterize(panel.getWidth() - 100, panel.getHeight() - 60, "Active solid: " + (activeSolidIndex + 1) + "/" + solids.size());
        textRasterizer.rasterize(panel.getWidth() - 120, panel.getHeight() - 40, "('M' for +1, 'N' for -1)");
        textRasterizer.rasterize(panel.getWidth() - 160, panel.getHeight() - 20, "Scales: X: " + rotationAxis.x + ", Y: " + rotationAxis.y + ", Z: " + rotationAxis.z);
        textRasterizer.rasterize(panel.getWidth() / 2 - 40, 60, hitCount + "/" + dummies.size() + " hit.", 30);
        DecimalFormat f = new DecimalFormat("#.0");
        textRasterizer.rasterize(panel.getWidth() / 2 - 70, 30, "Time: " + f.format(time) + "s", 30);
        textRasterizer.rasterize(panel.getWidth() - 150, 20, "Leaderboard", 20);
        for (LeaderboardTime time : leaderboard)
        {

        }
        panel.repaint();
    }
}
