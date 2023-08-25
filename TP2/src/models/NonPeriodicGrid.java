package models;

import java.util.Set;

public class NonPeriodicGrid extends Grid{

    public NonPeriodicGrid(double L, Set<Particle> particles, double rc) {
        super(L, particles, rc);
    }

    @Override
    public void CIM() {
        //TODO: implementar CIM bien
        for (int i=0; i<getM(); i++){
            for (int j=0; j<getM(); j++){
                Cell cell = getCells()[i][j];
                cell.getParticles().forEach(particle -> {
//                    int row = (int) Math.floor((particle.getX()%getL())/getRc());
//                    int col = (int) Math.floor((particle.getY()%getL())/getRc());
//                    getCells()[row][col].getParticles().forEach(particle1 -> {
//                        if (particle1.getId() != particle.getId()){
//                            particle.getNeighbours().add(particle1);
//                        }
//                    });
                });
            }
        }
    }

    @Override
    public void updatePositions(Particle p) {

    }

    @Override
    public void setAdjacentNeighbours() {
        // TODO: unnecesary method for off lattice simulation.
    }

}
