package models;

public class Limit {
    private static int counter = 1000;
    private final int id;
    private final double mass = 0;

    private final double radius = 0.0001;
    private final Velocity velocity = new Velocity(0, 0);

    private double x;
    private double y;

    public Limit( double x, double y) {
        this.id = counter;
        this.x = x;
        this.y = y;
        counter++;
    }

    public static int getCounter() {
        return counter;
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

    public Velocity getVelocity() {
        return velocity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx(){
        return velocity.getVx();
    }

    public double getVy(){
        return velocity.getVy();
    }

    
}
