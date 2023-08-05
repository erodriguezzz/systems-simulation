package TP1;

import java.util.ArrayList;
import java.util.List;

public class Particle {

    private String name;
    private List<Particle> neighbours; // This could be a Set to ensure the same particle is not added twice, but if the algorithm is implemented correctly it should not be necessary
    private double radius;
    public Particle(String name, double radius) {
        this.name = name;
        this.neighbours = new ArrayList<>();
        this.radius = radius;
    }

    public void addNeighbour(Particle particle) {
        neighbours.add(particle);
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Particle> getNeighbours() {
        return neighbours;
    }
}