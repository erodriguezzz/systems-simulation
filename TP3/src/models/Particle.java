package models;

import java.util.Objects;

public class Particle implements Comparable<Particle>{

    private static int counter = 1;
    private final int id;
    private final double mass;

    private final double radius;
    private Velocity velocity;

    private double x;
    private double y;

    public Particle(double radius, Velocity velocity, double x, double y, double mass) {
        this.radius = radius;
        this.id = counter;
        this.velocity = velocity;
        this.x = x;
        this.y = y;
        this.mass = mass;
        counter++;
    }

    /*
    * return the duration of time until the invoking particle collides with particle b,
    * assuming both follow straight-line trajectories.
    * If the two particles never collide, return a negative value.
     */
    public double timeToCollision(Particle b) {
        double dx = b.getX() - this.getX();
        double dy = b.getY() - this.getY();
        double dvx = b.getVx() - this.getVx();
        double dvy = b.getVy() - this.getVy();
        double dvdr = dx * dvx + dy * dvy;
        double dvdv = dvx * dvx + dvy * dvy;
        double drdr = dx * dx + dy * dy;
        double sigma = this.getRadius() + b.getRadius();
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);

        if (dvdr >= 0 || d < 0)
            return -1;

        if (drdr < sigma * sigma)
            throw new RuntimeException("overlapping particles");

        return -(dvdr + Math.sqrt(d)) / dvdv;
    }


    public double getRadius() {
        return radius;
    }


    public Velocity getVelocity() {
        return velocity;
    }

    public double getVx(){
        return velocity.getVx();
    }

    public double getVy(){
        return velocity.getVy();
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }


    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }


    public void updatePosition(double time) {
        this.x = this.x + getVelocity().getVx()*time;
        this.y = this.y + getVelocity().getVy()*time;
    }

    public int getId() {
        return id;
    }

    public double getMass() {
        return mass;
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
