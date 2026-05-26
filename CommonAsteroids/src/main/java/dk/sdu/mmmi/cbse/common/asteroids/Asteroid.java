package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Asteroid extends Entity {

    private int generation;

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = Math.max(0, generation);
    }
}
