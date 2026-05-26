package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    private static final int initialAsteroids = 5;
    private final Random random = new Random();

    // Creates initial asteroids when game starts.
    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < initialAsteroids; i++) {
            world.addEntity(createAsteroid(gameData, 28 + random.nextInt(15), 0));
        }
    }

    // Removes every asteroid in the game, when game stops.
    @Override
    public void stop(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    // Helper method that creates asteroids.
    private Entity createAsteroid(GameData gameData, float radius, int generation) {
        Asteroid asteroid = new Asteroid();
        asteroid.setGeneration(generation);
        asteroid.setPolygonCoordinates(-radius, -radius * 0.4, -radius * 0.2, -radius, radius * 0.8, -radius * 0.6,
                radius, radius * 0.2, radius * 0.2, radius, -radius * 0.8, radius * 0.5);
        asteroid.setX(random.nextDouble(gameData.getDisplayWidth()));
        asteroid.setY(random.nextDouble(gameData.getDisplayHeight()));
        asteroid.setRadius(radius);
        asteroid.setRotation(random.nextInt(360));
        asteroidMovement(asteroid, 0.6 + random.nextDouble() * 0.9);
        return asteroid;
    }

    // Asteroid movement.
    private void asteroidMovement(Entity asteroid, double speed) {
        double direction = Math.toRadians(asteroid.getRotation());
        asteroid.setVelocityX(Math.cos(direction) * speed);
        asteroid.setVelocityY(Math.sin(direction) * speed);
    }
}
