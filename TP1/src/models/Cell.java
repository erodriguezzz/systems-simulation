package models;

import java.util.HashSet;
import java.util.Set;

public class Cell {

    // TODO: podría ser útil tener una referencia a las celdas adjacentes para visitar los vecinos
    private Set<Particle> particles;

    public Cell() {
        this.particles = new HashSet<>();
    }

    public Set<Particle> getParticles() {
        return particles;
    }
}
