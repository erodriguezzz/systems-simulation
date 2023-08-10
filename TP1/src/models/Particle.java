package models;

import java.util.List;

public class Particle {
    private int id;
    private float x;
    private float y;
    private float radius;
    private List<Particle> neighbours;

    public Particle(int id, float x, float y, float radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        neighbours = new java.util.ArrayList<>();
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

    public void addNeighbour(Particle particle) {
        neighbours.add(particle);
    }

    public List<Particle> getNeighbours() {
        return neighbours;
    }

    @Override
    public String toString() {
        return "Particle{" + "id=" + id + ", x=" + x + ", y=" + y + ", radius=" + radius + '}';
    }

    public double distance(Particle particle) {
        return Math.sqrt(Math.pow(this.x - particle.x, 2) + Math.pow(this.y - particle.y, 2));
    }

    public void setNeighbours(List<Particle> neighbours) {
        this.neighbours = neighbours;
    }
}