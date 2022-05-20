package xyz.doodlejump.render;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    public static void rect(double x, double y, double width, double height, Color color) {
        glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        glBegin(GL_QUADS);
        glVertex2d(x, y);
        glVertex2d(x + width, y);
        glVertex2d(x + width, y + height);
        glVertex2d(x, y +height);
        glEnd();
    }
}
