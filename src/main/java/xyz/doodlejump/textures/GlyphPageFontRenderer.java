package xyz.doodlejump.textures;

import java.awt.*;
import org.lwjgl.opengl.*;
import java.util.*;

public class GlyphPageFontRenderer
{
    public Random fontRandom;
    private float posX;
    private float posY;
    private int[] colorCode;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    private GlyphPage regularGlyphPage;
    private GlyphPage boldGlyphPage;
    private GlyphPage italicGlyphPage;
    private GlyphPage boldItalicGlyphPage;

    public GlyphPageFontRenderer(final GlyphPage regularGlyphPage, final GlyphPage boldGlyphPage, final GlyphPage italicGlyphPage, final GlyphPage boldItalicGlyphPage) {
        this.fontRandom = new Random();
        this.colorCode = new int[32];
        this.regularGlyphPage = regularGlyphPage;
        this.boldGlyphPage = boldGlyphPage;
        this.italicGlyphPage = italicGlyphPage;
        this.boldItalicGlyphPage = boldItalicGlyphPage;
        for (int i = 0; i < 32; ++i) {
            final int j = (i >> 3 & 0x1) * 85;
            int k = (i >> 2 & 0x1) * 170 + j;
            int l = (i >> 1 & 0x1) * 170 + j;
            int i2 = (i & 0x1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i2 /= 4;
            }
            this.colorCode[i] = ((k & 0xFF) << 16 | (l & 0xFF) << 8 | (i2 & 0xFF));
        }
    }
    public static GlyphPageFontRenderer createSimpleFontRenderer(Font font) {
        char[] chars = new char[256];
        for(int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }
        GlyphPage gpage1 = new GlyphPage(font, true, false);
        gpage1.generateGlyphPage(chars);
        gpage1.setupTexture();
        return new GlyphPageFontRenderer(gpage1, gpage1, gpage1, gpage1);
    }

    public static GlyphPageFontRenderer create(final String fontName, final int size, final boolean bold, final boolean italic, final boolean boldItalic) {
        final char[] chars = new char[256];
        for (int i = 0; i < chars.length; ++i) {
            chars[i] = (char)i;
        }
        final GlyphPage regularPage = new GlyphPage(new Font(fontName, 0, size), true, true);
        regularPage.generateGlyphPage(chars);
        regularPage.setupTexture();
        GlyphPage boldPage = regularPage;
        GlyphPage italicPage = regularPage;
        GlyphPage boldItalicPage = regularPage;
        if (bold) {
            boldPage = new GlyphPage(new Font(fontName, 1, size), true, true);
            boldPage.generateGlyphPage(chars);
            boldPage.setupTexture();
        }
        if (italic) {
            italicPage = new GlyphPage(new Font(fontName, 2, size), true, true);
            italicPage.generateGlyphPage(chars);
            italicPage.setupTexture();
        }
        if (boldItalic) {
            boldItalicPage = new GlyphPage(new Font(fontName, 3, size), true, true);
            boldItalicPage.generateGlyphPage(chars);
            boldItalicPage.setupTexture();
        }
        return new GlyphPageFontRenderer(regularPage, boldPage, italicPage, boldItalicPage);
    }

    public int drawString(final String text, final double x, final double y, final Color color, final boolean dropShadow) {
        GL11.glEnable(6406);
        this.resetStyles();
        int i;
        if (dropShadow) {
            i = this.renderString(text, (float)(x + 1.0), (float)(y + 1.0), color, true);
            i = Math.max(i, this.renderString(text, (float)x, (float)y, color, false));
        }
        else {
            i = this.renderString(text, (float)x, (float)y, color, false);
        }
        return i;
    }

    private int renderString(final String text, final float x, final float y, Color color, final boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if (dropShadow) {
            color = Color.black;
        }
        this.red = color.getRed() / 255.0f;
        this.blue = color.getBlue() / 255.0f;
        this.green = color.getGreen() / 255.0f;
        this.alpha = color.getAlpha() / 255.0f;
        GL11.glColor4d(this.red, this.green, this.blue, this.alpha);
        this.posX = x * 2.0f;
        this.posY = y * 2.0f;
        this.renderStringAtPos(text, dropShadow);
        return (int)(this.posX / 4.0f);
    }

    private void renderStringAtPos(final String text, final boolean shadow) {
        GlyphPage glyphPage = this.getCurrentGlyphPage();
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3553);
        glyphPage.bindTexture();
        GL11.glTexParameteri(3553, 10240, 9729);
        for (int i = 0; i < text.length(); ++i) {
            glyphPage = this.getCurrentGlyphPage();
            glyphPage.bindTexture();
            final float f = glyphPage.drawChar(text.charAt(i), this.posX, this.posY);
            this.doDraw(f, glyphPage);

        }
        glyphPage.unbindTexture();
        GL11.glPopMatrix();
    }

    private void doDraw(final float f, final GlyphPage glyphPage) {
        if (this.strikethroughStyle) {
            GL11.glDisable(3553);
            GL11.glBegin(7);
            GL11.glVertex2d(this.posX, this.posY + glyphPage.getMaxFontHeight() / 2);
            GL11.glVertex2d(this.posX + f, this.posY + glyphPage.getMaxFontHeight() / 2);
            GL11.glVertex2d(this.posX + f, this.posY + glyphPage.getMaxFontHeight() / 2 - 1.0f);
            GL11.glVertex2d(this.posX, this.posY + glyphPage.getMaxFontHeight() / 2 - 1.0f);
            GL11.glEnd();
            GL11.glEnable(3553);
        }
        if (this.underlineStyle) {
            GL11.glDisable(3553);
            GL11.glBegin(7);
            final int l = this.underlineStyle ? -1 : 0;
            GL11.glVertex2d(this.posX + l, this.posY + glyphPage.getMaxFontHeight());
            GL11.glVertex2d(this.posX + f, this.posY + glyphPage.getMaxFontHeight());
            GL11.glVertex2d(this.posX + f, this.posY + glyphPage.getMaxFontHeight() - 1.0f);
            GL11.glVertex2d(this.posX + l, this.posY + glyphPage.getMaxFontHeight() - 1.0f);
            GL11.glEnd();
            GL11.glEnable(3553);
        }
        this.posX += f;
    }

    private GlyphPage getCurrentGlyphPage() {
        if (this.boldStyle && this.italicStyle) {
            return this.boldItalicGlyphPage;
        }
        if (this.boldStyle) {
            return this.boldGlyphPage;
        }
        if (this.italicStyle) {
            return this.italicGlyphPage;
        }
        return this.regularGlyphPage;
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    public int getFontHeight() {
        return this.regularGlyphPage.getMaxFontHeight() / 2;
    }

    public int getStringWidth(final String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        final int size = text.length();
        for (int i = 0; i < size; ++i) {
            final GlyphPage currentPage = this.getCurrentGlyphPage();
            width += (int)(currentPage.getWidth(text.charAt(i)) - 8.0f);

        }
        return width / 2;
    }

    public String trimStringToWidth(final String text, final int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(final String text, final int maxWidth, final boolean reverse) {
        final StringBuilder stringbuilder = new StringBuilder();
        final int j = reverse ? (text.length() - 1) : 0;
        final int k = reverse ? -1 : 1;
        double width = 0;
        for (int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
            char character = text.charAt(i);
            final GlyphPage currentPage = this.getCurrentGlyphPage();
            width += (int)((currentPage.getWidth(character) - 8.0f) / 2.0f) + 0.5;
            if (width >= maxWidth) {
                break;
            }
            if (reverse) {
                stringbuilder.insert(0, character);
            }
            else {
                stringbuilder.append(character);
            }
        }
        return stringbuilder.toString();
    }

    public int getTextColor() {
        return this.textColor;
    }

    public boolean isRandomStyle() {
        return this.randomStyle;
    }
}
