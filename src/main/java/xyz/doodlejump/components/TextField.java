package xyz.doodlejump.components;

import org.lwjgl.glfw.GLFW;
import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.textures.GlyphPageFontRenderer;
import xyz.doodlejump.render.RenderUtils;

import java.awt.*;
import java.util.function.Function;

public class TextField extends Component {

    public String text;
    public int lengthLimit = 128;

    public boolean focused;
    public Function<String, String> textFunction;

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

    @Override
    public void render() {

        final long now = System.currentTimeMillis();
        if (now - timer >= 500) {
            timer = now;
            blink ^= true;
        }

        RenderUtils.rect(posX - 1.0, posY - 1.0, width + 2.0, height + 2.0, Color.BLACK);
        RenderUtils.rect(posX, posY, width, height, Color.WHITE);
        String textureRender = glyphPageFontRenderer.trimStringToWidth(text, width);
        if(textFunction != null) textureRender = textFunction.apply(textureRender);
        if(blink && focused) textureRender += "_";
        glyphPageFontRenderer.drawString(textureRender, posX, posY, Color.BLACK, false);
    }

    @Override
    public void onMouseClick(int button, int action, int mods) {
        if(action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            focused = DoodleJump.mouseX >= posX && DoodleJump.mouseX <= posX + width && DoodleJump.mouseY >= posY && DoodleJump.mouseY <= posY + height;
        }
    }

    @Override
    public void onCharTyped(char c) {
        if(text.length() < lengthLimit && focused) {
            text += c;
        }
    }

    @Override
    public void onKey(int key, int scancode, int action, int mods) {
        if((action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) && key == GLFW.GLFW_KEY_BACKSPACE) {
            if(text.length() > 0 && focused) {
                text = text.substring(0, text.length() - 1);
            }
        }
    }
}
