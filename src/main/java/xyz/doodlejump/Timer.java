package xyz.doodlejump;

public class Timer {

    long timer;
    double tps;

    double elapsed;

    public Timer(double tps) {
        timer = System.nanoTime();
        this.tps = tps;
    }

    public int update() {
        long now = System.nanoTime();
        double millis = now / 1_000_000_000.0D;
        double lastMillis = timer / 1_000_000_000.0D;
        timer = now;
        elapsed += (millis - lastMillis) * tps;
        int ticks = (int) elapsed;
        elapsed -= ticks;
        return ticks;
    }

}
