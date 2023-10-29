package models;

import models.Grid;
import models.Particle;
import services.JsonConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeemanIntegrator{

    private final double dt;
    private final double frequency;
    private final List<Particle> particles;
    private final Grid grid;
    private final List<Double> times = new ArrayList<>();
    private final List<Double> energy = new ArrayList<>();
    private final JsonConfigurer config;

    public BeemanIntegrator(double dt, double d, double frequency, List<Particle> particles, JsonConfigurer config) {
        this.dt = dt;
        this.frequency = frequency;
        this.particles = particles;
        this.config = config;
        
        this.grid = new Grid(d, config);

        grid.addAll(this.particles);

    }

    public void run(double time) {

            grid.shake(time, frequency); //TODO

            particles.forEach(Particle::prediction);
            particles.forEach(Particle::resetForce);

            for (int j = 0; j < grid.update(); j++)
                times.add(time);

            grid.updateForces();

            particles.forEach(Particle::correction);

            particles.forEach(Particle::resetForce);
            grid.updateForces();

    }

    public List<Double> getTimes() {
        return times;
    }

    // public double getCaudal(){
    //     return times.size() / (iterations * dt);
    // }

    public List<Double> getEnergy() {
        return energy;
    }

    public List<Particle> getParticles() {
        return particles;
    }
}
