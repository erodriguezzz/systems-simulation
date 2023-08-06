package models;

public class Particle {
    private int id;
    private float x;
    private float y;
    private float radius;

    public Particle(int id, float x, float y, float radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Particle{" + "id=" + id + ", x=" + x + ", y=" + y + ", radius=" + radius + '}';
    }
}