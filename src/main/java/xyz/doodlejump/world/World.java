package xyz.doodlejump.world;

import xyz.doodlejump.entity.BoundingBox;
import xyz.doodlejump.entity.Entity;
import xyz.doodlejump.entity.Ground;
import xyz.doodlejump.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final List<Entity> entities = new ArrayList<>();
    private Player player;

    public void loadDefault() {
        player = new Player(this);
        player.setPosition(0.0, 400.0);
        this.spawn(player);

        Ground ground = new Ground(this);
        ground.setPosition(0.0, 150.0);
        this.spawn(ground);
    }

    public void tick() {
        for (Entity entity : entities) {
            entity.tick();
        }
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
