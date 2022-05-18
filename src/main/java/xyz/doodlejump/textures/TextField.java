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

    private long timer;
    private boolean blink;


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

        final long now = System.currentTimeMillis();
        if (now - timer >= 500) {
            timer = now;
            blink ^= true;
        }

        RenderUtils.rect(posX - 1.0, posY - 1.0, width + 2.0, height + 2.0, Color.BLACK);
        RenderUtils.rect(posX, posY, width, height, Color.WHITE);
        String textureRender = glyphPageFontRenderer.trimStringToWidth(text, width);
        if(blink) textureRender += "_";
        glyphPageFontRenderer.drawString(textureRender, posX, posY, Color.BLACK, false);
    }

    public void onMouseClick(int button, int action, int mods) {

    }

    public void onCharTyped(char c) {

    }

    public void onKey(int key, int scancode, int action, int mods) {

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
