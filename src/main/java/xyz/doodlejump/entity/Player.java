package xyz.doodlejump.entity;

import org.lwjgl.glfw.GLFW;
import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.GameState;
import xyz.doodlejump.world.World;

public class Player extends Entity {

    public Player(World world) {
        super(world);
        this.width = 65.0;
        this.height = 90.0;
    }

    @Override
    public void tick() {
        super.tick();

        if(y < -1000){
            y = 400;
            x = 0;
            motionX = 0;
            motionY = 0;
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
