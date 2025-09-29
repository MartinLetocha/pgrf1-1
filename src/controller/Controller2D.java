package controller;


import view.Panel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller2D {
    private final Panel panel;
    private int color = 0xFFFFFF;
    private final ArrayList<Integer> colors = new ArrayList<Integer>(Arrays.asList(0xFF00FF, 0xFFFF00, 0x00FFFF, 0xFFFFFF, 0xAAF000, 0xFFA000));
    private int colorIndex = 0;
    public Controller2D(Panel panel) {
        this.panel = panel;
        initListeners();
        //panel.getRaster().setRGB(50, 50, 0xffffff);
        panel.repaint();
    }

    private void initListeners()
    {
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                panel.getRaster().setRGB(me.getX(), me.getY(), color);
                panel.repaint();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent me) {
                var raster = panel.getRaster();
                if(me.getX() < 0 || me.getY() < 0 || me.getX() >= raster.getWidth() || me.getY() >= raster.getHeight())
                    return;
                raster.setRGB(me.getX(), me.getY(), color);
                panel.repaint();
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                var raster = panel.getRaster();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        for (int i = 0; i < raster.getWidth() / 2; i++) {
                            raster.setRGB(raster.getWidth()/2 + i, raster.getHeight()/2, color);
                        }
                        panel.repaint();
                        break;
                    case KeyEvent.VK_LEFT:
                        for (int i = 0; i < raster.getWidth() / 2; i++) {
                            raster.setRGB(raster.getWidth()/2 - i, raster.getHeight()/2, color);
                        }
                        panel.repaint();
                        break;
                    case KeyEvent.VK_UP:
                        for (int i = 0; i < raster.getHeight() / 2; i++) {
                            raster.setRGB(raster.getWidth()/2, raster.getHeight()/2 - i, color);
                        }
                        panel.repaint();
                        break;
                    case KeyEvent.VK_P:
                        colorIndex++;
                        if(colorIndex >= colors.size())
                        {
                            colorIndex = 0;
                        }
                        color = colors.get(colorIndex);
                        break;
                    case KeyEvent.VK_O:
                        colorIndex--;
                        if(colorIndex < 0)
                        {
                            colorIndex = colors.size() - 1;
                        }
                        color = colors.get(colorIndex);
                        break;
                    case KeyEvent.VK_C:
                        panel.cleanRaster();
                        break;
                }
            }
        });
    }

}
