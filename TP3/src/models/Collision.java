package models;

public class Collision implements Comparable<Collision> {
    private final Particle p1;
    private final Particle p2;
    private final double time;
    private final CollisionType type;

    public Collision(Particle p1, Particle p2, double time, CollisionType type) {
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
        this.type = type;
    }

    public Collision(Particle p1, double time, CollisionType type) {
        this.p1 = p1;
        this.p2 = null;
        this.time = time;
        this.type = type;
    }

    public void collide(double M, double L) {
        if (type != CollisionType.PARTICLE) {
            System.out.println("Type = " + type + " time = " + time);
            System.out.println("p1 = " + p1.getId() + " x = " + p1.getX() + " y = " + p1.getY() + " vx = " + p1.getVx() + " vy = " + p1.getVy() + " radius = " + p1.getRadius());
        }
        switch (type) {
            // TODO: handle pressure calculations
            case MID_WALL:
                p1.setVx(-p1.getVx());
                break;
            case RIGHT_WALL:
                p1.setVx(-p1.getVx());
                break;
            case LEFT_WALL:
                p1.setVx(-p1.getVx());
                break;
            case LEFT_UPPER_WALL:
            case LEFT_LOWER_WALL:
            case RIGHT_UPPER_WALL:
            case RIGHT_LOWER_WALL:
                p1.setVy(-p1.getVy());
                break;
            case PARTICLE:
                double x1 = p1.getX();
                double y1 = p1.getY();
                double vx1 = p1.getVx();
                double vy1 = p1.getVy();
                double radius1 = p1.getRadius();

                if (p2 == null) {
                    throw new IllegalArgumentException("p2 cannot be null if isWallCollision is false");
                }

                double x2 = p2.getX();
                double y2 = p2.getY();
                double vx2 = p2.getVx();
                double vy2 = p2.getVy();
                double radius2 = p2.getRadius();

                double dx = x2 - x1;
                double dy = y2 - y1;
                double dvx = vx2 - vx1;
                double dvy = vy2 - vy1;
                double dvdr = dx * dvx + dy * dvy;
                double dist = Math.sqrt(dx * dx + dy * dy);

                double sigma = radius1 + radius2;
                double tolerance = 1E-6;
                System.out.println(sigma - dist < tolerance ? "Ok" : "Sigma = " + sigma + " dist = " + dist);
                double J;
                if (p2.getMass() == Double.POSITIVE_INFINITY) {
                    J = (2 * p1.getMass() * dvdr) / ((p1.getMass()) * sigma);
                } else {
                    J = (2 * p1.getMass() * p2.getMass() * dvdr) / ((p1.getMass() + p2.getMass()) * sigma);
                }
                double Jx = J * dx / dist;
                double Jy = J * dy / dist;

                double newVx1 = vx1 + Jx / p1.getMass();
                double newVy1 = vy1 + Jy / p1.getMass();
                double newVx2 = vx2 - Jx / p2.getMass();
                double newVy2 = vy2 - Jy / p2.getMass();

                /*
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("dist = " + dist + " sigma = " + sigma);
                System.out.println("Old total vx = " + (vx1 + vx2) + " Total vy = " + (vy1 + vy2));
                System.out.println("New total vx = " + (newVx1 + newVx2) + " Total vy = " + (newVy1 + newVy2));
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                */
                p1.setVelocity(new Velocity(newVx1, newVy1));
                p2.setVelocity(new Velocity(newVx2, newVy2));
                break;
            default:
                throw new IllegalArgumentException("Invalid CollisionType");

        }
        if (type != CollisionType.PARTICLE) {
            System.out.println("p1 = " + p1.getId() + " x = " + p1.getX() + " y = " + p1.getY() + " vx = " + p1.getVx() + " vy = " + p1.getVy() + " radius = " + p1.getRadius());
        }


        // System.out.println();

    }

    public Particle getP1() {
        return this.p1;
    }

    public Particle getP2() {
        return this.p2;
    }

    public double getTime() {
        return this.time;
    }

    @Override
    public String toString() {
        return "Collision{" +
                "p1=" + p1 +
                ", p2=" + (p2==null? "wall":p2) +
                ", time=" + time +
                '}';
    }

    @Override
    public int compareTo(Collision o) {
        return Double.compare(this.time, o.time);
    }

    // Override equals method so that a collision is equal to another if they're both wall collisions involving the same particle or they're both ParticlCollisions involving the same two particles in any order
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Collision))
            return false;
        Collision collision = (Collision) o;
        if (type != collision.type)
            return false;
        if (type != CollisionType.PARTICLE) {
            return (p1.equals(collision.p1));
        } else {
            return (p1.equals(collision.p1) && (p2 != null && p2.equals(collision.p2))) || (p1.equals(collision.p2) && (p2 != null && p2.equals(collision.p1)));
        }
    }
}
