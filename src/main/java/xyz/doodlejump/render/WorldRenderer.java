package xyz.doodlejump.render;

import org.lwjgl.opengl.GL11;
import xyz.doodlejump.ColorChanger;
import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.entity.*;
import xyz.doodlejump.textures.Texture;
import xyz.doodlejump.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.glColor4d;

public class WorldRenderer {

    private final Texture playerTexture;
    private final Texture groundTexture;
    private final Texture playerBootsTexture;
    private final Texture bootsTexture;
    private final Texture trampTexture;
    private final Texture julianTexture;


    public WorldRenderer() {
        this.playerTexture = loadTexture("player");
        this.groundTexture = loadTexture("ground");
        this.playerBootsTexture = loadTexture("player_boots");
        this.bootsTexture = loadTexture("boots");
        this.trampTexture = loadTexture("tramp");
        this.julianTexture = loadTexture("julian_boss_mob");

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
                Player playerEntity = (Player) entity;
                if(playerEntity.hasBoots()){
                    texture = playerBootsTexture;
                } else {
                    texture = playerTexture;
                }
                break;
            case GROUND:
                texture = groundTexture;
                break;
            case JULIAN:
                texture = julianTexture;
                break;
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entity.getType());
        }

        
        double eW = entity.width;
        if(entity.getType() == EntityType.PLAYER && entity.left) {
            eW *= -1.0;
            x -= eW;
        }
        
        if(entity.getType() == EntityType.PLAYER) {
            if (entity.x + eW / 2.0 >= DoodleJump.width / 2.0) { // Right
                double d = (entity.x + eW / 2.0) - DoodleJump.width / 2.0;
                double leftSide = eW - d;
                texture.draw(leftSide * -1.0, y - entity.height, eW, entity.height, Color.white);
            } else if (entity.x - eW / 2.0 <= DoodleJump.width / -2.0) { // Left
                double d = (entity.x - eW / 2.0) - DoodleJump.width / -2.0;
                texture.draw(DoodleJump.width + d, y - entity.height, eW, entity.height);
            }
        }



        Color color = Color.WHITE;
        if(entity instanceof BreakingGround){
            color = new Color(ColorChanger.r, ColorChanger.g, ColorChanger.b);
        }else if(entity instanceof MovingGround) {
            color = new Color(50, 50, 255);
        }
        
        texture.draw(x, y - entity.height, eW, entity.height, color);


        if(entity instanceof Ground) {

            if(((Ground) entity).highJump){
                trampTexture.draw(x + 30, y -90, 636/7,520/7);
            }
            if(((Ground) entity).hasBoots){
                bootsTexture.draw(x + 20, y -90, 323/2.7,114/2.7);
            }
        }
        // RenderUtils.outline(x, y - entity.height, eW, entity.height, new Color(ColorChanger.r, ColorChanger.g, ColorChanger.b));
    }

    private Texture loadTexture(String name) {
        try {
            return new Texture(ImageIO.read(WorldRenderer.class.getResource("/assets/textures/" + name + ".png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Texture getPlayerBootsTexture() {
        return playerBootsTexture;
    }

    public Texture getBootsTexture() {
        return bootsTexture;
    }

    public Texture getGroundTexture() {
        return groundTexture;
    }

    public Texture getPlayerTexture() {
        return playerTexture;
    }

}
