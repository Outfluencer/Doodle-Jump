package xyz.doodlejump.entity;

import org.lwjgl.glfw.GLFW;
import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.GameState;
import xyz.doodlejump.components.Button;
import xyz.doodlejump.world.World;

public class Player extends Entity {

    public double highestPosition;

    public Player(World world) {
        super(world);
        this.width = 65.0;
        this.height = 90.0;
    }

    public int getScore(){
        return (int) (highestPosition / 10);
    }

    @Override
    public void tick() {
        super.tick();

        if(y > highestPosition){
            highestPosition = y;
        }
        if (y < highestPosition - DoodleJump.height * 1.3) {
            getWorld().despawn(this);
            DoodleJump.gameState = GameState.GAME_OVER;
            DoodleJump.components.add(new Button(DoodleJump.width / 2 - (360/ 2), DoodleJump.height / 2 + 100 , 360, 80, DoodleJump::setToStartUp, "RETRY", DoodleJump.GLYPH_PAGE_FONT_RENDERER_START));
        }


        if(x > DoodleJump.width / 2.0 || x < DoodleJump.width / -2.0){
            x = -x*0.99;
        }

        final boolean left = DoodleJump.isKeyPressed(GLFW.GLFW_KEY_LEFT) || DoodleJump.isKeyPressed(GLFW.GLFW_KEY_A);
        final boolean right = DoodleJump.isKeyPressed(GLFW.GLFW_KEY_RIGHT) || DoodleJump.isKeyPressed(GLFW.GLFW_KEY_D);
        int add = 8;
        if (!left || !right) {
            if(left) {
                motionX = -add;
            }else if(right) {
                motionX = add;
            }
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }
}
