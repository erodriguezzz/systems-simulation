package models;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private Set<Particle> particles = new HashSet<>();
    private Set<Cell> adjacentNeighbours = new HashSet<>();

    public Set<Particle> getParticles() {
        return particles;
    }

    public void setParticles(Set<Particle> particles) {
        this.particles = particles;
    }

    public Set<Cell> getAdjacentNeighbours() {
        return adjacentNeighbours;
    }

    public void setAdjacentNeighbours(Set<Cell> adjacentNeighbours) {
        this.adjacentNeighbours = adjacentNeighbours;
    }
}
