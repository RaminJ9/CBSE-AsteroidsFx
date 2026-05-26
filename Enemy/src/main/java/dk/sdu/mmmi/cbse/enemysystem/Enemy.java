package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Enemy extends Entity {
    private int framesUntilDirectionChange;

    public int getFramesUntilDirectionChange() {
        return framesUntilDirectionChange;
    }

    public void setFramesUntilDirectionChange(int framesUntilDirectionChange) {
        this.framesUntilDirectionChange = Math.max(0, framesUntilDirectionChange);
    }
}
