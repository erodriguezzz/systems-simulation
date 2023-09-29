package models;

import java.util.Objects;

public class Particle implements Comparable<Particle>{

    private static int counter = 1;
    private final int id;
    private final double mass;
    private double speed;
    private final double radius;
    private double acceleration;
    private double x;
    private Double previousX = null;

    public Particle(double radius, double x, double speed, double mass) {
        this.radius = radius;
        this.id = counter;
        this.x = x;
        this.speed = speed;
        this.mass = mass;
        counter++;
    }

    public double getRadius() {
        return radius;
    }

    public void setX(double x) {
        this.previousX = this.x;
        this.x = x;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
    public double getX() {
        return x;
    }

    public int getId() {
        return id;
    }

    public double getMass() {
        return mass;
    }

    @Override
    public int compareTo(Particle p2) {
        return Double.compare(this.x, p2.x);
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public Double getPreviousX() {
        return previousX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", mass=" + mass +
                ", speed=" + speed +
                ", radius=" + radius +
                ", acceleration=" + acceleration +
                ", x=" + x +
                ", previousX=" + previousX +
                '}';
    }
}
