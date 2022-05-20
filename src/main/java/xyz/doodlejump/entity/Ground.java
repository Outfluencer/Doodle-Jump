package xyz.doodlejump.entity;

import xyz.doodlejump.world.World;

public class Ground extends Entity {

    public Ground(World world) {
        super(world);
        this.width = 150;
        this.height = 50;
    }

    @Override
    public void tick() {
        this.updateBoundingBox();
    }

    @Override
    public EntityType getType() {
        return EntityType.GROUND;
    }
}
