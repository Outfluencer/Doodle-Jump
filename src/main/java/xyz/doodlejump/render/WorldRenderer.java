package xyz.doodlejump.render;

import xyz.doodlejump.ColorChanger;
import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.entity.Entity;
import xyz.doodlejump.textures.Texture;
import xyz.doodlejump.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class WorldRenderer {

    private final Texture playerTexture;
    private final Texture groundTexture;

    public WorldRenderer() {
        this.playerTexture = loadTexture("player");
        this.groundTexture = loadTexture("ground");
    }

    public void render() {
        final World world = DoodleJump.world;
        if(world == null) return;

        for (Entity entity : world.getEntities()) {
            this.renderEntity(entity);
        }
    }

    private void renderEntity(Entity entity) {
        double w = DoodleJump.width;
        double h = DoodleJump.height;
        double x = w / 2.0 + entity.x - entity.width / 2.0;
        double y = h - entity.y;

        Texture texture;
        switch (entity.getType()) {
            case PLAYER:
                texture = playerTexture;
                break;
            case GROUND:
                texture = groundTexture;
                break;
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entity.getType());
        }
        texture.draw(x, y - entity.height, entity.width, entity.height);
        RenderUtils.outline(x, y - entity.height, entity.width, entity.height, new Color(ColorChanger.r, ColorChanger.g, ColorChanger.b));
    }

    private Texture loadTexture(String name) {
        try {
            return new Texture(ImageIO.read(WorldRenderer.class.getResource("/assets/textures/" + name + ".png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
