package xyz.doodlejump.world;

import xyz.doodlejump.entity.BoundingBox;
import xyz.doodlejump.entity.Entity;
import xyz.doodlejump.entity.Ground;
import xyz.doodlejump.entity.Player;

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
    private double cameraAnim;

    public void loadDefault() {
        player = new Player(this);
        player.setPosition(0.0, 400.0);
        this.spawn(player);

        Ground ground = new Ground(this);
        ground.setPosition(0.0, 150.0);
        this.spawn(ground);

        this.spawn(new Ground(this, 0.0, 420.0));
        this.spawn(new Ground(this, -150.0, 420.0));
        this.spawn(new Ground(this, 150.0, 420.0));
    }

    public void tick() {
        for (Entity entity : entities) {
            entity.tick();
            if(entity != player) {
                if(entity.y - player.y < -400.0) {
                    this.despawn(entity);
                }
            }
        }

        cameraAnim = (player.y - 300.0) - cameraY;
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
            if(entity == source) continue;
            boxes.add(entity.boundingBox);
        }
        return boxes;
    }


    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }
}
