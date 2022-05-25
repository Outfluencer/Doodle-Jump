package xyz.doodlejump.entity;

import xyz.doodlejump.world.World;

public class Ground extends Entity {

    public boolean highJump = false;
    public Ground(World world) {
        this(world, 0.0, 0.0);
    }

    public Ground(World world, double x, double y) {
        super(world);
        this.width = 150;
        this.height = 50;
        this.setPosition(x, y);
    }

    public boolean isPlayerJumping = false;

    public boolean hasBoots = false;

    @Override
    public void tick() {
        this.updateBoundingBox();
        isPlayerJumping = boundingBox.isGoingToCollideFromTop(getWorld().getPlayer().boundingBox, getWorld().getPlayer().motionY);
        if(isPlayerJumping)
        {

            if(hasBoots){
                hasBoots = false;
                getWorld().getPlayer().bootstime = System.currentTimeMillis() + 12000;
            }

            getWorld().getPlayer().jump(highJump);
        }


    }

    @Override
    public EntityType getType() {
        return EntityType.GROUND;
    }
}
