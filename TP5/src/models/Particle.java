package models;

import java.math.RoundingMode;
import java.security.PublicKey;
import java.util.Objects;

public class Particle implements Comparable<Particle> {
    private int id;
    private double vx, vy, fx, fy;
    private double x, y;
    private double prevAx, prevAy;
    private double ax, ay;
    private double radius;
    private double mass;
    // private double vx;
    private double u;

    public Particle(int id, double x, double y, double mass, double radius){
        this.id = id;
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.radius = radius;
        this.prevAy = -9.8;
    }

    public Particle(int id, double x, double vx, double u, double radius, double mass, double XnoPeriodic) {
        this.id = id;
        this.x = x;
        this.radius = radius;
        this.mass = mass;
        // this.vx = vx;
        this.u = u;
    }

    public Particle(int id, double x, double vx, double u, double radius, double mass, double x2, double x3,
            double x4,
            double x5, double XnoPeriodic) {
        this.id = id;
        this.x = x;
        this.radius = radius;
        this.mass = mass;
        // this.vx = vx;
        this.u = u;
    }

    // public double collision(Particle p2) {
    // return 2500 * (Math.abs(this.getX() - p2.getX()) - (2 * this.getRadius()))
    // * (Math.signum(this.getX() - p2.getX()));
    // }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getU() {
        return u;
    }

    public int getId() {
        return id;
    }

    public void resetForce() {
        this.fx = 0;
        this.fy = 0;
    }

    public double evaluateAx() {
        return fx/mass;
    }

    public double evaluateAy() {
        return fy/mass;
    }

    public double getPrevAx() {
        return prevAx;
    }

    public void setPrevAx(double prevAx) {
        this.prevAx = prevAx;
    }

    public double getPrevAy() {
        return prevAy;
    }

    public void setPrevAy(double prevAy) {
        this.prevAy = prevAy;
    }

    public double distance(Particle p) {
        return Math.sqrt(Math.pow(this.getX() - p.getX(), 2) + Math.pow(this.getY() - p.getY(), 2));
    }

    public void addForces(double Fn, double Ft, Particle otherP) {
        double distance = this.distance(otherP);
        double eny = (getY() - otherP.getY()) / distance;
        double enx = (getX() - otherP.getX()) / distance;

        double Fx = Fn * enx - Ft * eny; //TODO check if vectors are correct
        double Fy = Fn * eny + Ft * enx;
        this.setFx(this.getFx() + Fx);
        this.setFy(this.getFy() + Fy);
        otherP.setFx(otherP.getFx() - Fx);
        otherP.setFy(otherP.getFy() - Fy);
    }

    public void addWallForce(double Fn, double xMulti, double yMulti){
        double forceX = Fn * xMulti; //TODO check if vectors are correct
        double forceY = Fn * yMulti;
        this.setFx(this.getFx() + forceX);
        this.setFy(this.getFy() + forceY);
    }

    public boolean collidesWith(Particle p, Double dt) {

        double dr = Math.signum(this.getX() - p.getX());
        double dv = Math.signum(this.getVx() - p.getVx());

        double sigma = this.radius + p.getRadius();

        double dvdr = (dr * dv);
        if (dvdr >= 0) {
            return false;
        }

        double dv2 = (dv * dv);
        double dr2 = (dr * dr);
        double d = Math.pow(dvdr, 2) - dv2 * (dr2 - Math.pow(sigma, 2));
        if (d < 0) {
            return false;
        }

        return (-(dvdr + Math.sqrt(d)) / dv2) < dt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setU(double u) {
        this.u = u;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", x=" + x +
                '}';
    }

    @Override
    public int compareTo(Particle p2) {
        return (int) Math.signum(this.x - p2.getX());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Particle other = (Particle) o;
        return id == other.id;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getFx() {
        return fx;
    }

    public void setFx(double fx) {
        this.fx = fx;
    }

    public double getFy() {
        return fy;
    }

    public void setFy(double fy) {
        this.fy = fy;
    }

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

}