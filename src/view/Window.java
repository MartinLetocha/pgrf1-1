package view;

import special.ColorGradient;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final Panel panel;
    private final String baseTitle = "PGRF1 2025/2026";

    public Window(int width, int heigth) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(baseTitle);
        setVisible(true);

        panel = new Panel(width, heigth);
        add(panel);
        pack();

        panel.setFocusable(true);
        panel.grabFocus();
    }
    public ColorGradient selectColor()
    {
        var color1 = JColorChooser.showDialog(this, "Choose a color.", Color.WHITE);
        var color2 = JColorChooser.showDialog(this, "Choose a second color.", color1);
        return new ColorGradient(color1, color2);
    }

    public Color selectBorderColor()
    {
        return JColorChooser.showDialog(this, "Choose the border color.", Color.WHITE);
    }

    public void changeTitle(String title) {
        setTitle(baseTitle + " " + title);
    }
    public Panel getPanel() {
        return panel;
    }
}

