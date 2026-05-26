package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

public class AsteroidSplitterImpl implements IAsteroidSplitter {

    private static final float minSplitRadius = 14f;

    // Splitter method, receives asteroid and splits it.
    @Override
    public void createSplitAsteroid(Entity source, World world) {
        if (!(source instanceof Asteroid asteroid) || source.getRadius() <= minSplitRadius) {
            return;
        }

        float newRadius = Math.max(8f, source.getRadius() * 0.58f);
        for (int i = 0; i < 2; i++) {
            Asteroid child = new Asteroid();
            child.setGeneration(asteroid.getGeneration() + 1);
            child.setRadius(newRadius);
            child.setX(source.getX());
            child.setY(source.getY());
            child.setRotation(source.getRotation() + (i == 0 ? 35 : -35));
            child.setPolygonCoordinates(-newRadius, -newRadius * 0.4, -newRadius * 0.1, -newRadius,
                    newRadius * 0.9, -newRadius * 0.5, newRadius, newRadius * 0.3,
                    newRadius * 0.1, newRadius, -newRadius * 0.9, newRadius * 0.4);
            double speed = 1.2 + asteroid.getGeneration() * 0.25;
            double angle = Math.toRadians(child.getRotation());
            child.setVelocityX(Math.cos(angle) * speed);
            child.setVelocityY(Math.sin(angle) * speed);
            world.addEntity(child);
        }
    }
}
