package models;

public class Velocity {
    private double velocity;
    private double theta;

    public Velocity(double velocity, double theta) {
        this.velocity = velocity;
        this.theta = theta;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getVX() {
        return velocity * Math.cos(theta);
    }

    public double getVY() {
        return velocity * Math.sin(theta);
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
