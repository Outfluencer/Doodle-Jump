package xyz.doodlejump.entity;

import xyz.doodlejump.world.World;

import java.util.List;

public abstract class Entity implements Comparable<Entity> {

    private final World world;
    public BoundingBox boundingBox;
    public double x, y;
    public double width, height;
    public double motionX, motionY;
    public boolean onGround, collidedHorizontally, collidedVertically;

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

        double newX = motionX;
        double newY = motionY;

        final List<BoundingBox> boxes = world.getCollisionBoxes(this);
        if(newY != 0.0) {
            for (BoundingBox box : boxes) {
                newY = box.collideY(this.boundingBox, newY);
            }
            if(newY != 0.0) {
                boundingBox.offset(0.0, newY);
            }
        }

        this.x += newX;
        this.y += newY;

        collidedHorizontally = newX != motionX;
        collidedVertically = newY != motionY;
        onGround = motionY < 0.0 && collidedVertically;

        if(newY != motionY) motionY = 0.0;
        this.updateBoundingBox();
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
