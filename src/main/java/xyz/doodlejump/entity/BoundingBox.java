package xyz.doodlejump.entity;

public class BoundingBox {

    public double minX;
    public double minY;
    public double maxX;
    public double maxY;

    public BoundingBox(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public BoundingBox() {
    }

    public boolean intersects(BoundingBox other) {
        return !(minX > other.maxX || maxX < other.minX || minY > other.maxY || maxY < other.minY);
    }

    public boolean contains(double x, double y) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    public double collideY(BoundingBox other, double motionY) {
        if(other.minX < maxX && other.maxX > minX) {
            if(motionY > 0.0 && other.minY < maxY) {
                final double deltaY = other.minY - maxY;
                return Math.min(deltaY, motionY);
            }
        }
        return motionY;
    }

    public double collideX(BoundingBox other, double motionX) {
        if(other.minY < maxY && other.maxY > minY) {
            if(motionX > 0.0 && other.minX < maxX) {
                final double deltaX = other.minX - maxX;
                return Math.min(deltaX, motionX);
            }
        }
        return motionX;
    }

    public void offset(double x, double y) {
        minX += x;
        minY += y;
        maxX += x;
        maxY += y;
    }
}
