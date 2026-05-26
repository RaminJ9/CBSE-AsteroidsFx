package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

public class CollisionDetector implements IPostEntityProcessingService {

    // list of entities and which that have been removed.
    // and overall checks collision and handles it.
    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());
        Set<String> handled = new HashSet<>();

        for (int i = 0; i < entities.size(); i++) {
            Entity first = entities.get(i);
            if (handled.contains(first.getID()) || world.getEntity(first.getID()) == null) {
                continue;
            }
            for (int j = i + 1; j < entities.size(); j++) {
                Entity second = entities.get(j);
                if (handled.contains(second.getID()) || world.getEntity(second.getID()) == null) {
                    continue;
                }
                if (collides(first, second)) {
                    handleCollision(first, second, gameData, world, handled);
                }
            }
        }
    }

    // check if overlap.
    public boolean collides(Entity entity1, Entity entity2) {
        double dx = entity1.getX() - entity2.getX();
        double dy = entity1.getY() - entity2.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < entity1.getRadius() + entity2.getRadius();
    }

    // What kind of collision.
    private void handleCollision(Entity first, Entity second, GameData gameData, World world, Set<String> handled) {
        if (first instanceof Bullet bullet && second instanceof Asteroid asteroid) {
            bulletHitsAsteroid(bullet, asteroid, gameData, world, handled);
        } else if (second instanceof Bullet bullet && first instanceof Asteroid asteroid) {
            bulletHitsAsteroid(bullet, asteroid, gameData, world, handled);
        } else if (first instanceof Bullet bullet) {
            bulletHitsEntity(bullet, second, world, handled);
        } else if (second instanceof Bullet bullet) {
            bulletHitsEntity(bullet, first, world, handled);
        } else if (first instanceof Asteroid || second instanceof Asteroid) {
            destroyShipInAsteroidCollision(first, second, world, handled);
        }
    }

    private void bulletHitsAsteroid(Bullet bullet, Asteroid asteroid, GameData gameData, World world, Set<String> handled) {
        world.removeEntity(bullet);
        world.removeEntity(asteroid);
        handled.add(bullet.getID());
        handled.add(asteroid.getID());

        loadAsteroidSplitter().forEach(splitter -> splitter.createSplitAsteroid(asteroid, world));

        long points = asteroid.getRadius() > 18 ? 10 : 25;
        gameData.addScore(points);
        gameData.incrementDestroyedAsteroids();
    }

    private void bulletHitsEntity(Bullet bullet, Entity target, World world, Set<String> handled) {
        if (target instanceof Bullet || target.getID().equals(bullet.getOwnerId())
                || target.getClass().getName().equals(bullet.getOwnerType())) {
            return;
        }

        target.damage(bullet.getDamage());
        world.removeEntity(bullet);
        handled.add(bullet.getID());

        if (target.isDead()) {
            world.removeEntity(target);
            handled.add(target.getID());
        }
    }

    private void destroyShipInAsteroidCollision(Entity first, Entity second, World world, Set<String> handled) {
        Entity ship = first instanceof Asteroid ? second : first;
        if (ship instanceof Bullet || ship instanceof Asteroid) {
            return;
        }
        world.removeEntity(ship);
        handled.add(ship.getID());
    }

    private List<IAsteroidSplitter> loadAsteroidSplitter() {
        return ServiceLoader.load(IAsteroidSplitter.class).stream().map(ServiceLoader.Provider::get).toList();
    }
}
