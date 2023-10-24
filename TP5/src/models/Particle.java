package models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.PublicKey;
import java.util.Objects;

public class Particle implements Comparable<Particle> {
    private int id;
    private BigDecimal vx, vy, fx, fy;
    private BigDecimal x, y;
    private BigDecimal prevAx, prevAy;
    private BigDecimal ax, ay;
    private BigDecimal radius;
    private BigDecimal mass;
    // private double vx;
    private double u;

    public Particle(int id, BigDecimal x, BigDecimal y, BigDecimal mass, BigDecimal radius){
        this.id = id;
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.radius = radius;
        this.vx = BigDecimal.ZERO;
        this.vy = BigDecimal.ZERO;
        this.fx = BigDecimal.ZERO;
        this.fy = BigDecimal.ZERO;
        this.prevAx = BigDecimal.ZERO;
        this.prevAy = BigDecimal.valueOf(-9.8);
        this.ax = BigDecimal.ZERO;
        this.ay = BigDecimal.ZERO;
    }

    public Particle(int id, BigDecimal x, double vx, double u, BigDecimal radius, BigDecimal mass, double XnoPeriodic) {
        this.id = id;
        this.x = x;
        this.radius = radius;
        this.mass = mass;
        // this.vx = vx;
        this.u = u;
    }

    public Particle(int id, BigDecimal x, double vx, double u, BigDecimal radius, BigDecimal mass, double x2, double x3,
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

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public BigDecimal getRadius() {
        return radius;
    }

    public BigDecimal getMass() {
        return mass;
    }

    public BigDecimal getVx() {
        return vx;
    }

    public void setVx(BigDecimal vx) {
        this.vx = vx;
    }

    public double getU() {
        return u;
    }

    public int getId() {
        return id;
    }

    public void resetForce() {
        this.fx = BigDecimal.ZERO;
        this.fy = BigDecimal.ZERO;
    }

    public BigDecimal evaluateAx() {
        return fx.divide(mass);
    }

    public BigDecimal evaluateAy() {
        return fy.divide(mass);
    }

    public BigDecimal getPrevAx() {
        return prevAx;
    }

    public void setPrevAx(BigDecimal prevAx) {
        this.prevAx = prevAx;
    }

    public BigDecimal getPrevAy() {
        return prevAy;
    }

    public void setPrevAy(BigDecimal prevAy) {
        this.prevAy = prevAy;
    }

    public BigDecimal distance(Particle p) {
        return BigDecimal.valueOf(Math.sqrt(Math.pow(this.getX().subtract(p.getX()).doubleValue(), 2)
                + Math.pow(this.getY().subtract(p.getY()).doubleValue(), 2)));
    }

    public void addForces(BigDecimal Fn, BigDecimal Ft, Particle otherP) {
        BigDecimal distance = this.distance(otherP);
        BigDecimal forceX = Fn.multiply(this.getX().subtract(otherP.getX())).divide(distance, 10, RoundingMode.HALF_UP); //TODO check if vectors are correct
        BigDecimal forceY = Fn.multiply(this.getY().subtract(otherP.getY())).divide(distance, 10, RoundingMode.HALF_UP);
        this.setFx(this.getFx().add(forceX));
        this.setFy(this.getFy().add(forceY));
        otherP.setFx(otherP.getFx().subtract(forceX));
        otherP.setFy(otherP.getFy().subtract(forceY));

        BigDecimal forceXt = Ft.multiply(this.getY().subtract(otherP.getY())).divide(distance, 10, RoundingMode.HALF_UP);
        BigDecimal forceYt = Ft.multiply(this.getX().subtract(otherP.getX())).divide(distance, 10, RoundingMode.HALF_UP);
        this.setFx(this.getFx().add(forceXt));
        this.setFy(this.getFy().add(forceYt));
        otherP.setFx(otherP.getFx().subtract(forceXt));
        otherP.setFy(otherP.getFy().subtract(forceYt));
    }

    public void addWallForce(BigDecimal Fn, BigDecimal xMulti, BigDecimal yMulti){
        BigDecimal forceX = Fn.multiply(xMulti); //TODO check if vectors are correct
        BigDecimal forceY = Fn.multiply(yMulti);
        this.setFx(this.getFx().add(forceX));
        this.setFy(this.getFy().add(forceY));
    }

    public boolean collidesWith(Particle p, Double dt) {
        double dr = this.getX().compareTo(p.getX());
        double dv = this.getVx().compareTo(p.getVx());

        double sigma = this.radius.add(p.getRadius()).doubleValue();

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

    public void setRadius(BigDecimal radius) {
        this.radius = radius;
    }

    public void setMass(BigDecimal mass) {
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
        return this.x.compareTo(p2.getX());
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

    public BigDecimal getVy() {
        return vy;
    }

    public void setVy(BigDecimal vy) {
        this.vy = vy;
    }

    public BigDecimal getFx() {
        return fx;
    }

    public void setFx(BigDecimal fx) {
        this.fx = fx;
    }

    public BigDecimal getFy() {
        return fy;
    }

    public void setFy(BigDecimal fy) {
        this.fy = fy;
    }

    public BigDecimal getAx() {
        return ax;
    }

    public void setAx(BigDecimal ax) {
        this.ax = ax;
    }

    public BigDecimal getAy() {
        return ay;
    }

    public void setAy(BigDecimal ay) {
        this.ay = ay;
    }

}