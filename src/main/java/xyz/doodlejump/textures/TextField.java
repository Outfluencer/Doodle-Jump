package xyz.doodlejump.textures;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class TextField {

    public String text;

    public int posX;
    public int posY;

    public int width;
    public int height;


    public GlyphPageFontRenderer glyphPageFontRenderer;

    public TextField(String text, int posX, int posY, int width, int height, GlyphPageFontRenderer glyphPageFontRenderer) {
        this.text = text;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.glyphPageFontRenderer = glyphPageFontRenderer;
    }

    public void render() {

        glColor4d(1,1,1,1);
        glBegin(GL_QUADS);
        glVertex2d(posX, posY);
        glVertex2d(posX + width, posY);
        glVertex2d(posX + width, posY + height);
        glVertex2d(posX, posY +height);
        glEnd();
        String textureRender = glyphPageFontRenderer.trimStringToWidth(text, width);
        glyphPageFontRenderer.drawString(textureRender, posX, posY, Color.BLACK, false);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setGlyphPageFontRenderer(GlyphPageFontRenderer glyphPageFontRenderer) {
        this.glyphPageFontRenderer = glyphPageFontRenderer;
    }



}
