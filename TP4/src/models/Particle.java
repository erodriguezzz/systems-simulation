package models;

import java.util.Objects;

public class Particle implements Comparable<Particle>{

    private static int counter = 1;
    private final int id;
    private final double mass;
    private double speed;
    private final double radius;
    // private double acceleration;
    private double x, ax, x3, x4, x5;
    private Double previousX = null;

    public Particle(double radius, double x, double speed, double mass) {
        this.radius = radius;
        this.id = counter;
        this.x = x;
        this.speed = speed;
        this.mass = mass;
        counter++;
        this.x3=0;
        this.x4=0;
        this.x5=0;
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
//                ", acceleration=" + acceleration +
                ", x=" + x +
                ", previousX=" + previousX +
                '}';
    }



    public static int getCounter() {
        return counter;
    }



    public static void setCounter(int counter) {
        Particle.counter = counter;
    }



    public double getAx() {
        return ax;
    }



    public void setAx(double ax) {
        this.ax = ax;
    }



    public double getX3() {
        return x3;
    }



    public void setX3(double x3) {
        this.x3 = x3;
    }



    public double getX4() {
        return x4;
    }



    public void setX4(double x4) {
        this.x4 = x4;
    }



    public double getX5() {
        return x5;
    }



    public void setX5(double x5) {
        this.x5 = x5;
    }



    public void setPreviousX(Double previousX) {
        this.previousX = previousX;
    }
}
