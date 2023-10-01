package models2;

public class Particle implements Comparable<Particle>{

    private int id;
    private double x, mass, vx, radius, u, ax;

    public Particle(int id, double x, double vx, double u, double radius, double m) {
        this.id = id;
        this.x = x;
        this.mass = m;
        this.vx = vx;
        this.u = u;
        this.radius = radius;
    }

    public double getCollisionForce(Particle p){
        return 2500*(Math.abs(p.getX() - this.getX()) - 2*this.getRadius())*Math.signum(p.getX() - this.getX());
    }

    public boolean isColliding(Particle p, Double dt) {

        double dx = p.getX() - this.getX();
        double dvx = p.getVx() - this.getVx();
        double dvdr = dx * dvx;
        double dvdv = dvx * dvx ;
        double drdr = dx * dx;
        double sigma = this.getRadius() + p.getRadius();
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);

        if (dvdr >= 0 || d < 0)
            return false;

        return (-(dvdr + Math.sqrt(d)) / dvdv ) < dt;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        double aux = x % 135;
        if (aux < 0){
            aux += 135;
        }
        this.x = aux;
    }

    public double getForce(){
        return u - vx;
    }
    
    public double getMass() {
        return mass;
    }
    public void setMass(double m) {
        this.mass = m;
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
    public void setU(double u) {
        this.u = u;
    }
    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Particle other = (Particle) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int compareTo(Particle p) {
        return this.id - p.id;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + x +
                ", m=" + mass +
                ", vx=" + vx +
                '}';
    }
}
