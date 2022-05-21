package xyz.doodlejump.entity;

import xyz.doodlejump.world.World;

public class BreakingGround extends Ground {

    public BreakingGround(World world) {
        this(world, 0.0, 0.0);
    }

    public BreakingGround(World world, double x, double y) {
        super(world);
        this.width = 150;
        this.height = 50;
        this.setPosition(x, y);
    }



    @Override
    public void tick() {
        this.updateBoundingBox();
        if(this.boundingBox.isGoingToCollideFromTop(getWorld().getPlayer().boundingBox, getWorld().getPlayer().motionY)){
            getWorld().despawn(this);
            getWorld().getPlayer().jump();
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.GROUND;
    }
}
