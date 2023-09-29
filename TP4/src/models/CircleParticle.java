package models;

import java.util.Objects;

public class CircleParticle implements Comparable<CircleParticle>{

    private static int counter = 1;
    private final int id;
    private final double mass;

    private final double radius;
    private double velocity;

    private double theta;

    public CircleParticle(double radius, double theta, double mass, double velocity) {
        this.radius = radius;
        this.id = counter;
        this.velocity = velocity;
        this.theta = theta;
        this.mass = mass;
        counter++;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        CircleParticle.counter = counter;
    }

    public int getId() {
        return id;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    @Override
    public int compareTo(CircleParticle p2) {
        return this.id - p2.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircleParticle particle = (CircleParticle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
