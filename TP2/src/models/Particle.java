package models;

import java.util.Objects;

public class Particle implements Comparable<Particle>{

    private static int counter = 1;
    private final int id;
    private final double radius;
    private Velocity velocity;

    private double x;
    private double y;

    public Particle(double radius, Velocity velocity, double x, double y) {
        this.radius = radius;
        this.id = counter;
        this.velocity = velocity;
        this.x = x;
        this.y = y;
        counter++;
    }


    public double getRadius() {
        return radius;
    }


    public Velocity getVelocity() {
        return velocity;
    }


    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }


    public double getTheta() {
        return this.velocity.getTheta();
    }


    public void setTheta(int theta) {
        this.velocity.setTheta(theta);
    }


    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }


    public void updatePosition(double time) {
        this.x = this.x + getVelocity().getVX()*time;
        this.y = this.y + getVelocity().getVY()*time;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Particle p2) {
        return this.id - p2.id;
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
}
