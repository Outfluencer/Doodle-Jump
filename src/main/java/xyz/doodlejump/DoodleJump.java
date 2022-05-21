package xyz.doodlejump;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import xyz.doodlejump.components.Button;
import xyz.doodlejump.components.Component;
import xyz.doodlejump.components.TextField;
import xyz.doodlejump.mysql.Communication;
import xyz.doodlejump.render.WorldRenderer;
import xyz.doodlejump.textures.GlyphPageFontRenderer;
import xyz.doodlejump.textures.Texture;
import xyz.doodlejump.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class DoodleJump {

    public static GameState gameState = GameState.LOGIN;
    public static boolean isProcessRunning = true;
    public static long windowHandle;

    public static Texture lol;
    public static WorldRenderer worldRenderer;
    public static World world;

    public static final List<Component> components = new CopyOnWriteArrayList<>();
    public static TextField usernameField;
    public static TextField passwordField;

    public static final int width = 400, height = 700;

    public static double mouseX, mouseY;

    public static String username;

    public static Button registerButton = null;

    public static void startWindow() throws IOException {
        if (!glfwInit()) {
            throw new RuntimeException("Can't init GLFW");
        }
        GLFWErrorCallback.createPrint(System.err).set();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        windowHandle = glfwCreateWindow(width, height, "Doodle Jump", 0, 0);

        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
        lol = new Texture(ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\player.png")));
        GLYPH_PAGE_FONT_RENDERER = GlyphPageFontRenderer.create("Comic Sans MS", 180, false, false, false);
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES = GlyphPageFontRenderer.create("Comic Sans MS", 30, false, false, false);
        GLYPH_PAGE_FONT_RENDERER_START = GlyphPageFontRenderer.create("Comic Sans MS", 100, false, false, false);
        // 1 = VSYNC, 0 = infinit fps;
        glfwSwapInterval(1);

        worldRenderer = new WorldRenderer();

        usernameField = new TextField("", width / 2 - (260 / 2), height / 2, 260, 28, GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);
        usernameField.lengthLimit = 16;
        components.add(usernameField);

        passwordField = new TextField("", width / 2 - (260 / 2), height / 2 + 64, 260, 28, GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);
        passwordField.lengthLimit = 16;
        passwordField.textFunction = text -> text.replaceAll(".", "*");
        components.add(passwordField);

        Button loginButton = new Button(width / 2 - 75, height / 2 + 128, 150, 50, () -> {
            // Try Login
            if(Communication.tryLogin(usernameField.text, passwordField.text)) {
                setToStartUp();
            }

        }, "Login", GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);
        Button createAccountButton = new Button(width / 2 - 75, height / 2 + 128, 150, 50, () -> {
            if(Communication.tryRegister(usernameField.text, passwordField.text)) {
                setToStartUp();
            }
        }, "Create Account", GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);


        Button goBackButton = new Button(width / 2 - 75, height / 2 + 128 + 80, 150, 50, () -> {
            components.clear();
            components.add(usernameField);
            components.add(passwordField);
            gameState = GameState.LOGIN;
            components.add(loginButton);
            components.add(registerButton);

        }, "Go Back", GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);

        registerButton = new Button(width / 2 - 75, height / 2 + 128 + 80, 150, 50, () -> {
            gameState = GameState.REGISTER;
            components.remove(loginButton);
            components.removeIf(component -> component instanceof Button && ((Button) component).text.equals("Register now!"));
            components.add(createAccountButton);
            components.add(goBackButton);

        }, "Register now!", GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES);

        components.add(loginButton);
        components.add(registerButton);

        glfwSetCharCallback(windowHandle, (window, codepoint) -> {
            components.forEach(textField -> textField.onCharTyped((char) codepoint));
        });
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {



            components.forEach(textField -> textField.onKey(key, scancode, action, mods));
        });
        glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
        });
        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            components.forEach(textField -> textField.onMouseClick(button, action, mods));
        });


        glClearColor(245 / 255.0f, 230 / 255.0f, 218 / 255.0f, 1);
        Timer timer = new Timer(60);
        while (isProcessRunning) {
            if (glfwWindowShouldClose(windowHandle)) {
                isProcessRunning = false;
            }
            int ticks = timer.update();
            ColorChanger.changeColor();
            if (gameState == GameState.RUNNING || gameState == GameState.START_GAME) {
                for (int i = 0; i < Math.min(ticks, 10); i++) {
                    runGameTick();
                }
            }
            render();
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }
    }

    public static boolean isKeyPressed(int key) {
        return glfwGetKey(windowHandle, key) != 0;
    }

    public static void setToStartUp() {
        username = usernameField.text;
        gameState = GameState.START_GAME;
        components.clear();
        Button b =  new Button(width / 2 - (360/ 2), height / 2 - 80, 360, 80, () -> {
            components.clear();
            gameState = GameState.RUNNING;
            loadGame();
        }, "START GAME", GLYPH_PAGE_FONT_RENDERER_START);
        b.rgb = true;
        components.add(b);
    }

    private static void loadGame() {
        world = new World();
        world.loadDefault();
    }

    public static void runGameTick() {
        if(world != null) {
            world.tick();
        }
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
        if (gameState == GameState.RUNNING) {
            drawGameScreen();
        }

        if (gameState == GameState.LOGIN) {
            drawLoginScreen();
        }

        if (gameState == GameState.REGISTER) {
            drawRegisterScreen();
        }

        if (gameState == GameState.START_GAME) {
            drawStartScreen();
        }
        if (gameState == GameState.GAME_OVER) {
            drawGameOver();
        }


        components.forEach(Component::render);

    }


    public static void drawBackground() {
        glColor4d(255 / 255.0d, 165 / 255.0d, 158 / 255.0d, 1);
        glLineWidth(1.0f);
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

        glLineWidth(3.0f);
        glBegin(GL_LINES);
        glVertex2d(3 * space, 0.0f);
        glVertex2d(3 * space, height);
        glEnd();
    }

    public static double x, y;

    public static void drawGameScreen() {
        worldRenderer.render();
    }

    public static GlyphPageFontRenderer GLYPH_PAGE_FONT_RENDERER;
    public static GlyphPageFontRenderer GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES;
    public static GlyphPageFontRenderer GLYPH_PAGE_FONT_RENDERER_START;

    public static void drawLoginScreen() {
        int size = GLYPH_PAGE_FONT_RENDERER.getStringWidth("Login");
        GLYPH_PAGE_FONT_RENDERER.drawString("Login", width / 2 - size / 2, height / 4, Color.yellow, true);

        size = GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.getStringWidth("Username");
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.drawString("Username", width / 2 - size / 2, height / 2 - 30, Color.BLUE, true);

        size = GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.getStringWidth("Password");
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.drawString("Password", width / 2 - size / 2, height / 2 + 40, Color.BLUE, true);
    }

    public static void drawRegisterScreen() {
        int size = GLYPH_PAGE_FONT_RENDERER.getStringWidth("Register");
        GLYPH_PAGE_FONT_RENDERER.drawString("Register", width / 2 - size / 2, height / 4, Color.yellow, true);

        size = GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.getStringWidth("Username");
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.drawString("Username", width / 2 - size / 2, height / 2 - 30, Color.BLUE, true);

        size = GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.getStringWidth("Password");
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.drawString("Password", width / 2 - size / 2, height / 2 + 40, Color.BLUE, true);
    }


    public static void drawStartScreen() {
        int size = GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.getStringWidth(username);

        int w = 49;
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.drawString(username, width / 2 - size / 2, height - 120 + (Math.sin(System.currentTimeMillis() / 50) * 20), Color.BLUE, true);
        lol.draw(width / 2 - (w / 2), height - 100 + (Math.sin(System.currentTimeMillis() / 50) * 20), w, 744 / 12);
    }


    public static void drawGameOver() {
        int size = GLYPH_PAGE_FONT_RENDERER.getStringWidth("GAME");
        GLYPH_PAGE_FONT_RENDERER.drawString("GAME", width / 2 - size / 2, height / 4, Color.RED, true);
        size = GLYPH_PAGE_FONT_RENDERER.getStringWidth("OVER");
        GLYPH_PAGE_FONT_RENDERER.drawString("OVER", width / 2 - size / 2, height / 4+ 100, Color.RED, true);

        size = GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.getStringWidth("Score: " + world.getPlayer().getScore());
        GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.drawString("Score: " + world.getPlayer().getScore(), width / 2 - size / 2, height / 2 + 200, new Color(ColorChanger.r, ColorChanger.g, ColorChanger.b), true);
    }

}
