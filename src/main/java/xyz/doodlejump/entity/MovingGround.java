package xyz.doodlejump.entity;

import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.world.World;

import java.util.Random;

public class MovingGround extends Ground {

    private final Random random;
    private double direction;

    public MovingGround(World world) {
        this(world, 0.0, 0.0);
    }

    public MovingGround(World world, double x, double y) {
        super(world);
        this.random = new Random();
        this.width = 150;
        this.height = 50;
        this.setPosition(x, y);
    }

    @Override
    public void tick() {
        if(direction == 0.0) {
            direction = random.nextBoolean() ? 5.0 : -5.0;
        }
        if(this.x + direction < DoodleJump.width / -2.0 + width / 2.0) {
            this.x = DoodleJump.width / -2.0 + width / 2.0;
            this.direction *= -1.0;
        }else if(this.x + direction > DoodleJump.width / 2.0 - width / 2.0) {
            this.x = DoodleJump.width / 2.0 - width / 2.0;
            this.direction *= -1.0;
        }
        this.x += direction;
        this.updateBoundingBox();
    }
}
