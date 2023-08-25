package models;

import java.sql.Array;
import java.util.Set;

public class PeriodicGrid extends Grid{

    public PeriodicGrid(double L, Set<Particle> particles, double rc) {
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

    /**
     * This method sets the adjacent cell neighbours of each cell,
     * taking into account the periodicity of the grid
     * and the 'L' shape for the CIM
     * For reference, go to TP2/img/neighbor_cell.png
     */
    @Override
    public void setAdjacentNeighbours() {
        for (int i = 0; i<getM(); i++) {
            for (int j = 0; j<getM(); j++) {
                // using % M to return to the first column/row when the last one is reached
                // or to go to the last when the first is reached
                getCells()[i][j].getAdjacentNeighbours().add(getCells()[i][(j+1)%getM()]); //1
                getCells()[i][j].getAdjacentNeighbours().add(getCells()[(i+1)%getM()][(j+1)%getM()]); //2
                getCells()[i][j].getAdjacentNeighbours().add(getCells()[(i+1)%getM()][j]); //3
                getCells()[i][j].getAdjacentNeighbours().add(getCells()[(i+1)%getM()][(j-1)%getM()]); //4
            }
        }
    }

    // TODO: update particle position in grid considering neighbour cells
    @Override
    public void updatePositions(Particle p) {

    }
}
