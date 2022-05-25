package xyz.doodlejump.world;

import xyz.doodlejump.DoodleJump;
import xyz.doodlejump.entity.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {

    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private Player player;

    public SecureRandom secureRandom = new SecureRandom();
    public double cameraY;


    int spawnAtY = 20;

    public void loadDefault() {
        player = new Player(this);
        player.setPosition(0.0, 0.0);
        this.spawn(player);

        Ground ground = new Ground(this);
        ground.setPosition(0.0, -250.0);
        this.spawn(ground);

        cameraY = -250.0;
        spawnNew();
    }

    public void spawnNew() {
        int type = secureRandom.nextInt(5);
        Ground firstGround = type == 4 ? new BreakingGround(this) : type == 3 ? new MovingGround(this) : new Ground(this);
        boolean highjump = secureRandom.nextInt(20) == 3;
        boolean boots = secureRandom.nextInt(40) == 3;
        firstGround.hasBoots = boots;
        if(!boots){
            firstGround.highJump = highjump;
        }
        double groundW = firstGround.width;

        boolean spawnTwo = secureRandom.nextBoolean() && secureRandom.nextBoolean();
        double xSpawnNew = (DoodleJump.width / -2.0 + groundW / 2.0) + secureRandom.nextInt(DoodleJump.width - (int) groundW);
        double ySpawn = spawnAtY + (10 - secureRandom.nextInt(20));

        firstGround.setPosition(xSpawnNew, ySpawn);
        this.spawn(firstGround);

        if (spawnTwo) {
            type = secureRandom.nextInt(5);
            boots = secureRandom.nextInt(40) == 3;
            if(!boots){
                highjump = secureRandom.nextInt(20) == 3;
            }else{
                highjump = false;
            }
            while (true) {
                Ground ground = type == 4 ? new BreakingGround(this) : type == 3 ? new MovingGround(this) : new Ground(this);
                firstGround.hasBoots = boots;
                ground.highJump = highjump;
                groundW = ground.width;
                xSpawnNew = (DoodleJump.width / -2.0 + groundW / 2.0) + secureRandom.nextInt(DoodleJump.width - (int) groundW);
                ySpawn = spawnAtY + (10 - secureRandom.nextInt(20));
                ground.setPosition(xSpawnNew, ySpawn);
                if (!ground.boundingBox.intersects(firstGround.boundingBox)) {
                    this.spawn(ground);
                    break;
                }
            }
        }

        spawnAtY += 150;
    }

    public void tick() {
        for (Entity entity : entities) {
            entity.tick();
            if (entity != player) {
                if (entity.y + entity.height < cameraY) {
                    this.despawn(entity);
                }
            }
        }

        while(entities.size() < 50) {
            spawnNew();
        }

        double cameraAnim = (player.y - 300.0) - cameraY;
        cameraAnim /= 10.0;
        cameraY += cameraAnim;
    }

    public void spawn(Entity entity) {
        entities.add(entity);
        entities.sort(null);
    }

    public void despawn(Entity entity) {
        entities.remove(entity);
    }

    public List<BoundingBox> getCollisionBoxes(Entity source) {
        final List<BoundingBox> boxes = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity == source) continue;
            boxes.add(entity.boundingBox);
        }
        return boxes;
    }

        // aa||
    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }
}
