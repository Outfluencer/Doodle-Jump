package xyz.doodlejump.render;

import org.lwjgl.opengl.GL11;
import xyz.doodlejump.ColorChanger;
import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.entity.BreakingGround;
import xyz.doodlejump.entity.Entity;
import xyz.doodlejump.entity.EntityType;
import xyz.doodlejump.entity.Player;
import xyz.doodlejump.textures.Texture;
import xyz.doodlejump.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.glColor4d;

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
        GL11.glPushMatrix();
        GL11.glTranslated(0.0, world.cameraY, 0.0);
        for (Entity entity : world.getEntities()) {
            this.renderEntity(entity);
        }
        GL11.glPopMatrix();


        DoodleJump.GLYPH_PAGE_FONT_RENDERER_TEXT_BOXES.drawString("Score: " + world.getPlayer().getScore(), 0, 5, Color.BLUE, true);
    }

    private void renderEntity(Entity entity) {

        Player player = entity.getWorld().getPlayer();
        double playerPos = player.y;

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


        if(entity.getType() == EntityType.PLAYER) {
            if (entity.x + entity.width / 2.0 >= DoodleJump.width / 2.0) { // Right
                double d = (entity.x + entity.width / 2.0) - DoodleJump.width / 2.0;
                double leftSide = entity.width - d;
                texture.draw(leftSide * -1.0, y - entity.height, entity.width, entity.height, Color.white);
            } else if (entity.x - entity.width / 2.0 <= DoodleJump.width / -2.0) { // Left
                double d = (entity.x - entity.width / 2.0) - DoodleJump.width / -2.0;
                texture.draw(DoodleJump.width + d, y - entity.height, entity.width, entity.height);
            }
        }



        if(entity instanceof BreakingGround){
            texture.draw(x, y - entity.height, entity.width, entity.height, ColorChanger.getColor());

        }else{
            texture.draw(x, y - entity.height, entity.width, entity.height);
        }
       // RenderUtils.outline(x, y - entity.height, entity.width, entity.height, new Color(ColorChanger.r, ColorChanger.g, ColorChanger.b));
    }

    private Texture loadTexture(String name) {
        try {
            return new Texture(ImageIO.read(WorldRenderer.class.getResource("/assets/textures/" + name + ".png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
