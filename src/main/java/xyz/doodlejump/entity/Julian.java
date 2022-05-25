package xyz.doodlejump.entity;

import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.world.World;

public class Julian extends Entity {


    public Julian(World world) {
        this(world, 0);

    }

    public final double startY;

    public Julian(World world, int posY){
        super(world);
        double sizeDiff = 6;
        this.width = 538 / sizeDiff;
        this.height = 640 / sizeDiff;
        setPosition(0, posY);
        startY = posY;
    }


    @Override
    public void tick() {
        updateBoundingBox();
        y = startY + Math.sin((DoodleJump.currentTick / 10d)) * 52;
        x = Math.cos((DoodleJump.currentTick / 50d)) * 220;
        if(getWorld().getPlayer().boundingBox.intersects(boundingBox)){
            getWorld().getPlayer().die();
        }
    }



    @Override
    public EntityType getType() {
        return EntityType.JULIAN;
    }
}
