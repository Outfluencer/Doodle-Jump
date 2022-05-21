package xyz.doodlejump.entity;

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
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }
}
