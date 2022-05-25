package xyz.doodlejump.entity;

import xyz.doodlejump.world.World;

import java.util.List;

public abstract class Entity implements Comparable<Entity> {

    private final World world;
    public BoundingBox boundingBox;
    public double x, y;
    public double width, height;
    public double motionX, motionY;
    public boolean left;

    public Entity(World world) {
        this.world = world;
        this.boundingBox = new BoundingBox();
    }

    public void tick() {
        this.updateBoundingBox();
        motionX *= 0.9;
        motionY -= 0.8;
        motionY *= 0.98;
        if(Math.abs(motionX) < 0.005) motionX = 0.0;
        if(Math.abs(motionY) < 0.005) motionY = 0.0;

        if(motionX != 0.0) {
            left = motionX < 0.0;
        }

        this.x += motionX;
        this.y += motionY;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        this.updateBoundingBox();
    }

    public void updateBoundingBox() {
        boundingBox.minX = x - width / 2.0;
        boundingBox.minY = y;
        boundingBox.maxX = x + width / 2.0;
        boundingBox.maxY = y + height;
    }

    public abstract EntityType getType();

    public World getWorld() {
        return world;
    }

    @Override
    public int compareTo(Entity o) {
        return this.getType().getId() - o.getType().getId();
    }
}
