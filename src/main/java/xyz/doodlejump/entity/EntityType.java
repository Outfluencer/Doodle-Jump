package xyz.doodlejump.entity;

public enum EntityType {

    GROUND(0),
    PLAYER(1),

    JULIAN(2);

    private int id;

    EntityType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
