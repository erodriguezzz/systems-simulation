package models;

import java.util.Set;

public class Cell {
    private Set<Particle> particles;
    private Set<Cell> adjacentNeighbours;

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
