package xyz.doodlejump.entity;

import xyz.doodlejump.world.World;

import java.util.List;

public abstract class Entity {

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
        motionX *= 0.98;
        motionY -= 0.2;
        motionY *= 0.98;
        if(Math.abs(motionX) < 0.005) motionX = 0.0;
        if(Math.abs(motionY) < 0.005) motionY = 0.0;

        double newX = motionX;
        double newY = motionY;

        final List<BoundingBox> boxes = world.getCollisionBoxes(this.boundingBox);
        if(newY != 0.0) {
            for (BoundingBox box : boxes) {
                newY = box.collideY(this.boundingBox, newY);
            }
            if(newY != 0.0) {
                boundingBox.offset(0.0, newY);
            }
        }
        if(newX != 0.0) {
            for (BoundingBox box : boxes) {
                newX = box.collideX(this.boundingBox, newX);
            }
            if(newX != 0.0) {
                boundingBox.offset(newX, 0.0);
            }
        }

        this.x += newX;
        this.y += newY;

        collidedHorizontally = newX != motionX;
        collidedVertically = newY != motionY;
        onGround = motionY < 0.0 && collidedVertically;

        if(newY != motionX) motionX = 0.0;
        if(newY != motionY) motionY = 0.0;

        this.updateBoundingBox();
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void updateBoundingBox() {
        boundingBox.minX = x - width / 2.0;
        boundingBox.minY = y;
        boundingBox.maxX = x + width / 2.0;
        boundingBox.maxY = y + height;
    }

    public World getWorld() {
        return world;
    }
}
