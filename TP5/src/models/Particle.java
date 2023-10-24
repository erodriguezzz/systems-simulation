package models;

import java.math.BigDecimal;
import java.util.Objects;

public class Particle implements Comparable<Particle> {
    private int id;
    private BigDecimal vx, vy, fx, fy;
    private BigDecimal x, y;
    private double x2, x3, x4, x5, XnoPeriodic;
    private double radius;
    private BigDecimal mass;
    // private double vx;
    private double u;

    public Particle(int id, BigDecimal x, double vx, double u, double radius, BigDecimal mass, double XnoPeriodic) {
        this.id = id;
        this.x = x;
        this.x2 = 0;
        this.x3 = 0;
        this.x4 = 0;
        this.x5 = 0;
        this.XnoPeriodic = XnoPeriodic;
        this.radius = radius;
        this.mass = mass;
        // this.vx = vx;
        this.u = u;
    }

    public Particle(int id, BigDecimal x, double vx, double u, double radius, BigDecimal mass, double x2, double x3, double x4,
            double x5, double XnoPeriodic) {
        this.id = id;
        this.x = x;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.x5 = x5;
        this.radius = radius;
        this.mass = mass;
        // this.vx = vx;
        this.u = u;
        this.XnoPeriodic = XnoPeriodic;
    }

    // public double collision(Particle p2) {
    //     return 2500 * (Math.abs(this.getX() - p2.getX()) - (2 * this.getRadius()))
    //             * (Math.signum(this.getX() - p2.getX()));
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

    public double getRadius() {
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

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
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

    public int getId() {
        return id;
    }

    public void resetForce() {
        this.fx = BigDecimal.ZERO;
        this.fy = BigDecimal.ZERO;
    }

    

    public boolean collidesWith(Particle p, Double dt) {
        double dr = this.getX().compareTo(p.getX());
        double dv = this.getVx().compareTo(p.getVx());

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

    public double getXnoPeriodic() {
        return XnoPeriodic;
    }

    public void setXnoPeriodic(double xnoPeriodic) {
        XnoPeriodic = xnoPeriodic;
    }

    public void setRadius(double radius) {
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

}