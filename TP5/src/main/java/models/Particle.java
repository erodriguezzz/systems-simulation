package models;

import models.Pair;
import models.Color;
import services.ForcesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Particle {
    private static final double ZERO = 0.0;
    private final static double B = (2.0 / 3.0);
    private final static double C = -(1.0 / 6.0);
    private final Pair force;
    private final Double radius;
    private final Double mass;
    private final int id;
    private boolean reInjected = false;
    private Color color = Color.RED;
    public Map<Particle, Double> acumVelocity = new HashMap<>();
    public Double[] wallAcum = {0.0,0.0,0.0,0.0};
    // BOTOTM, TOP, LEFT, RIGHT

    // Beeman information
    private final Double dt;
    private final Double sqrDt;
    private Pair position;
    private Pair velocity;
    private Pair prevAcceleration;
    private Pair actualAcceleration;
    private Pair actualVelocity;
    private boolean gone = false;

    public void resetForce() {
        force.setX(ZERO);
        force.setY(ZERO);
    }

    public double getEnergy(){
        return Math.pow(this.velocity.module(Pair.ZERO), 2) * mass / 2.0;
    }

    public void addToForce(double x, double y) {
        force.setX(force.getX() + x);
        force.setY(force.getY() + y);
    }

    public double getAccumVel(Particle p){
        return acumVelocity.get(p);
    }

    public double getAccumVelWall(int index){
        return wallAcum[index];
    }

    public void addAcumVel(Particle b, double relativeVel){
        double currentAcumVel = acumVelocity.get(b);
        this.acumVelocity.put(b, currentAcumVel + relativeVel);

        b.acumVelocity.put(this, currentAcumVel + relativeVel);
    }

    public void addAcumVelWall(int index, double relativeVel){
        double currentAcumVel = this.wallAcum[index];
        this.wallAcum[index] = currentAcumVel + relativeVel;
    }

    public void resetAcumVel(Particle b){
        this.acumVelocity.put(b, 0.0);
        b.acumVelocity.put(this, 0.0);
    }

    public void resetAcummWall(int index){
        this.wallAcum[index] = 0.0;
    }

    public Particle(int id, Pair position, Double radius, Double mass, Double dt, Color color) {
        this.id = id;
        this.position = position;
        this.radius = radius;
        this.mass = mass;
        this.force = new Pair(ZERO, ZERO);
        this.velocity = new Pair(ZERO, ZERO);
        this.dt = dt;
        this.sqrDt = Math.pow(dt, 2);
        actualAcceleration = new Pair(ZERO, ZERO);
        prevAcceleration = new Pair(ZERO, ForcesUtils.GRAVITY);
        this.color = color;
    }

    public Particle(double id, double x, double y, double mass, double radius){
        this.id = (int) id;
        this.position = new Pair(x, y);
        this.radius = radius;
        this.mass = mass;
        this.force = new Pair(ZERO, ZERO);
        this.velocity = new Pair(ZERO, ZERO);
        this.dt = 1.0E-3;
        this.sqrDt = Math.pow(dt, 2);
        actualAcceleration = new Pair(ZERO, ZERO);
        prevAcceleration = new Pair(ZERO, ForcesUtils.GRAVITY);
        this.color = Color.BLUE;
    }

    // public Particle copy() {
    //     return new Particle(id, position, radius, mass, dt, color);
    // }

    public void addToForce(Pair pair) {
        force.setX(force.getX() + pair.getX());
        force.setY(force.getY() + pair.getY());
    }

    public Pair getAcceleration() {
        return force.scale(1.0 / mass);
    }

    public void reInject() {
        reInjected = true;
        setColor(Color.RED);
    }

    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + velocity.getX() + " " + velocity.getY() + " " + radius + " " + color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public Pair getPosition() {
        return position;
    }

    public Pair getVelocity() {
        return velocity;
    }

    // BEEMAN
    public void prediction() {
//        this.actualAcceleration = this.getAcceleration();
        actualAcceleration = this.getAcceleration();
        this.position = position.sum(
                velocity.scale(dt).sum(
                        actualAcceleration.scale(B).sum(
                                prevAcceleration.scale(C)
                        ).scale(sqrDt)
                )
        );

        this.actualVelocity = velocity;

        this.velocity = this.actualVelocity.sum(
                this.actualAcceleration.scale(1.5 * dt).sum(
                        prevAcceleration.scale(-0.5 * dt)
                )
        );

        // sout everything
    //    System.out.println("Particle " + id + " position: " + position + " velocity: " + velocity + " acceleration: " + actualAcceleration);

    }

    public void correction(){
        if (reInjected){
            this.velocity = new Pair(ZERO, ZERO);
            reInjected = false;
            prevAcceleration = new Pair(ZERO, ForcesUtils.GRAVITY);
        }else {
            this.velocity = actualVelocity.sum(
                    this.getAcceleration().scale((1.0 / 3.0) * dt).sum(
                            actualAcceleration.scale((5.0 / 6.0) * dt).sum(
                                    prevAcceleration.scale(-(1.0 / 6.0) * dt)
                            )
                    )
            );
            prevAcceleration = actualAcceleration;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle)) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public boolean isGone() {
        return gone;
    }

    public void setGone(boolean gone) {
        this.gone = gone;
    }
}
