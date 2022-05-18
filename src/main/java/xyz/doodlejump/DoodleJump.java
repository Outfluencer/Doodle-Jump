package xyz.doodlejump;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import xyz.doodlejump.textures.GlyphPageFontRenderer;
import xyz.doodlejump.textures.TextField;
import xyz.doodlejump.textures.Texture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class DoodleJump {

    public static GameState gameState = GameState.LOGIN;
    public static boolean isProcessRunning = true;

    public static Texture lol;

    public static final List<TextField> textFields = new ArrayList<>();
    public static TextField usernameField;
    public static TextField passwordField;

    public static final int width = 400, height = 700;

    public static double mouseX, mouseY;

    public static void startWindow() throws IOException {
        if (!glfwInit()) {
            throw new RuntimeException("Can't init GLFW");
        }
        GLFWErrorCallback.createPrint(System.err).set();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long windowHandle = glfwCreateWindow(width, height, "Doodle Jump", 0, 0);

        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
        lol = new Texture(ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\Screenshot_1.png")));
        GLYPH_PAGE_FONT_RENDERER = GlyphPageFontRenderer.create("Comic Sans MS", 180, false, false, false);
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES = GlyphPageFontRenderer.create("Comic Sans MS", 30, false, false, false);

        // 1 = VSYNC, 0 = infinit fps;
        glfwSwapInterval(1);

        usernameField = new TextField("", width / 2 - (260 / 2), height / 2, 260, 28, GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);
        usernameField.lengthLimit = 16;
        textFields.add(usernameField);

        passwordField = new TextField("", width / 2 - (260 / 2), height / 2 + 64, 260, 28, GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);
        passwordField.lengthLimit = 16;
        passwordField.textFunction = text -> text.replaceAll(".", "*");
        textFields.add(passwordField);

        glfwSetCharCallback(windowHandle, (window, codepoint) -> {
            textFields.forEach(textField -> textField.onCharTyped((char) codepoint));
        });
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            textFields.forEach(textField -> textField.onKey(key, scancode, action, mods));
        });
        glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
        });
        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            textFields.forEach(textField -> textField.onMouseClick(button, action, mods));
        });


        glClearColor(245 / 255.0f, 230 / 255.0f, 218 / 255.0f, 1);
        Timer timer = new Timer(60);
        while (isProcessRunning) {
            if (glfwWindowShouldClose(windowHandle)) {
                isProcessRunning = false;
            }
            int ticks = timer.update();
            if (gameState == GameState.RUNNING) {
                for (int i = 0; i < Math.min(ticks, 10); i++) {
                    runGameTick();
                }
            }
            render();
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }
    }

    public static void runGameTick() {


    }

    public static void render() {
        glClear(GL_COLOR_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, 0, 1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glDisable(GL_CULL_FACE);


        drawBackground();
        lol.draw(10, 10, 50, 50);

    }


    public static void drawBackground() {
        glColor4d(255 / 255.0d, 165 / 255.0d, 158 / 255.0d, 1);
        glBegin(GL_LINES);

        int space = 18;

        for (int x = 0; x < width; x += space) {
            glVertex2d(x, 0);
            glVertex2d(x, height);
        }

        for (int y = 0; y < height; y += space) {
            glVertex2d(0, y);
            glVertex2d(width, y);
        }
        glEnd();


        if (gameState == GameState.RUNNING) {
            drawGameScreen();
        }

        if (gameState == GameState.LOGIN) {
            drawLoginScreen();
        }
    }

    public static double x, y;

    public static void drawGameScreen() {
    }

    public static GlyphPageFontRenderer GLYPH_PAGE_FONT_RENDERER;
    public static GlyphPageFontRenderer GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES;

    public static void drawLoginScreen() {
        int size = GLYPH_PAGE_FONT_RENDERER.getStringWidth("Login");
        GLYPH_PAGE_FONT_RENDERER.drawString("Login", width / 2 - size / 2, height / 4, Color.yellow, true);
        textFields.forEach(TextField::render);
        ///         textField = new TextField("", width / 2 - 100, height / 2, 200, 20, GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);
        size = GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.getStringWidth("Username");
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.drawString("Username", width / 2 - size / 2, height / 2 - 30, Color.BLUE, true);

    }

}
