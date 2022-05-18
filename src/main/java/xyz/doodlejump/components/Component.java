package xyz.doodlejump.components;

public class Component {

    public int posX;
    public int posY;

    public int width;
    public int height;

    public Component() {}

    public Component(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void render() {}

    public void onMouseClick(int button, int action, int mods) {}

    public void onCharTyped(char c) {}

    public void onKey(int key, int scancode, int action, int mods) {}
}
