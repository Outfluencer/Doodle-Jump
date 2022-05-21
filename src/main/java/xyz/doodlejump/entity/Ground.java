package xyz.doodlejump.entity;

import xyz.doodlejump.world.World;

public class Ground extends Entity {

    public Ground(World world) {
        this(world, 0.0, 0.0);
    }

    public Ground(World world, double x, double y) {
        super(world);
        this.width = 150;
        this.height = 50;
        this.setPosition(x, y);
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
