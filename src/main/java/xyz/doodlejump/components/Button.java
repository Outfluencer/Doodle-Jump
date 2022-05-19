package xyz.doodlejump.components;

import org.lwjgl.glfw.GLFW;
import xyz.doodlejump.ColorChanger;
import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.textures.GlyphPageFontRenderer;
import xyz.doodlejump.textures.RenderUtils;

import java.awt.*;

public class Button extends Component {

    public Runnable onClick;
    public String text;
    public GlyphPageFontRenderer glyphPageFontRenderer;
    private boolean hovered;

    public boolean rgb = false;

    public Button(int posX, int posY, int width, int height, Runnable onClick, String text, GlyphPageFontRenderer glyphPageFontRenderer) {
        super(posX, posY, width, height);
        this.onClick = onClick;
        this.text = text;
        this.glyphPageFontRenderer = glyphPageFontRenderer;
    }

    public void click() {
        onClick.run();
    }

    @Override
    public void onMouseClick(int button, int action, int mods) {
        if(hovered && action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.click();
        }
    }

    @Override
    public void render() {
        hovered = DoodleJump.mouseX >= posX && DoodleJump.mouseX <= posX + width && DoodleJump.mouseY >= posY && DoodleJump.mouseY <= posY + height;
        RenderUtils.rect(posX, posY, width, height, Color.BLACK);
        RenderUtils.rect(posX, posY, width - 1.0, height - 1.0, hovered ? new Color(200, 200, 255) : new Color(200, 200, 200));
        glyphPageFontRenderer.drawString(text, posX + width / 2.0 - glyphPageFontRenderer.getStringWidth(text) / 2.0, posY + height / 2.0 - glyphPageFontRenderer.getFontHeight() / 2.0,
                rgb ? new Color(ColorChanger.r, ColorChanger.g, ColorChanger.b) : Color.BLACK, rgb);
    }
}
