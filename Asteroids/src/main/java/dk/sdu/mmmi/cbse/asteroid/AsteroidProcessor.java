package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
// Game Loop code for asteroids.
public class AsteroidProcessor implements IEntityProcessingService {

    // Updates position of asteroid
    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            asteroid.setX(asteroid.getX() + asteroid.getVelocityX());
            asteroid.setY(asteroid.getY() + asteroid.getVelocityY());
            asteroid.setRotation(asteroid.getRotation() + 0.25);
            wrap(asteroid, gameData);
        }
    }

    // Border Control method
    private void wrap(Entity entity, GameData gameData) {
        if (entity.getX() < 0) {
            entity.setX(gameData.getDisplayWidth());
        } else if (entity.getX() > gameData.getDisplayWidth()) {
            entity.setX(0);
        }

        if (entity.getY() < 0) {
            entity.setY(gameData.getDisplayHeight());
        } else if (entity.getY() > gameData.getDisplayHeight()) {
            entity.setY(0);
        }
    }
}
