package xyz.doodlejump.world;

import xyz.doodlejump.entity.BoundingBox;
import xyz.doodlejump.entity.Entity;
import xyz.doodlejump.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final List<Entity> entities = new ArrayList<>();
    private Player player;

    public void loadDefault() {
        player = new Player(this);
        player.setPosition(0.0, 0.0);
    }

    public void tick() {
        for (Entity entity : entities) {
            entity.tick();
        }
    }

    public void spawn(Entity entity) {
        entities.add(entity);
    }

    public void despawn(Entity entity) {
        entities.remove(entity);
    }

    public List<BoundingBox> getCollisionBoxes(BoundingBox source) {
        return new ArrayList<>();
    }
}
