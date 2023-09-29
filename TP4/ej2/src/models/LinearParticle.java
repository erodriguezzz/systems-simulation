package models;

import java.util.Objects;

public class LinearParticle implements Comparable<LinearParticle>{

    private static int counter = 1;
    private final int id;
    private double mass, radius, force;
    private double vx;
    private double x;
    private double u;     
    private Double previousX = null;

   


    public LinearParticle(int id, double x, double vx, double radius, double mass) {
        this.id = id;
        this.mass = mass;
        this.radius = radius;
        this.force = 0;
        this.vx = vx;
        this.x = x;
    }
    public static int getCounter() {
        return counter;
    }
    public static void setCounter(int counter) {
        LinearParticle.counter = counter;
    }
    public int getId() {
        return id;
    }
    public double getMass() {
        return mass;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }
    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
    public double getForce() {
        return force;
    }
    public void setForce(double force) {
        this.force = force;
    }
    public double getVx() {
        return vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getU() {
        return u;
    }
    public void setU(double u) {
        this.u = u;
    }
    public Double getPreviousX() {
        return previousX;
    }
    public void setPreviousX(Double previousX) {
        this.previousX = previousX;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LinearParticle other = (LinearParticle) obj;
        if (id != other.id)
            return false;
        return true;
    }


    @Override
    public int compareTo(LinearParticle o) {
        // TODO Auto-generated method stub
        return 0;
    }

    
    
}
