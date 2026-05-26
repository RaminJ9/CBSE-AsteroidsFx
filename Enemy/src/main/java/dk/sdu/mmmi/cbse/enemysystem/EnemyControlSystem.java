package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {

    private static final double speed = 1.2;
    private static final double fireProbability = 0.008;
    private static final int maxEnemies = 2;
    private static final long enemySpawnInterval = 3000L;

    private final Random random = new Random();
    private long lastEnemySpawnNanos = System.currentTimeMillis();

    @Override
    public void process(GameData gameData, World world) {
        spawnEnemyIfReady(gameData, world);

        for (Entity entity : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;
            if (enemy.getFramesUntilDirectionChange() <= 0) {
                enemy.setRotation(random.nextInt(360));
                enemy.setFramesUntilDirectionChange(45 + random.nextInt(90));
            } else {
                enemy.setFramesUntilDirectionChange(enemy.getFramesUntilDirectionChange() - 1);
            }

            double angle = Math.toRadians(enemy.getRotation());
            enemy.setVelocityX(Math.cos(angle) * speed);
            enemy.setVelocityY(Math.sin(angle) * speed);
            enemy.setX(enemy.getX() + enemy.getVelocityX());
            enemy.setY(enemy.getY() + enemy.getVelocityY());
            wrap(enemy, gameData);

            if (random.nextDouble() < fireProbability) {
                getBulletSPIs().stream().findFirst().ifPresent(spi -> world.addEntity(spi.createBullet(enemy, gameData)));
            }
        }
    }

    private void spawnEnemyIfReady(GameData gameData, World world) {
        if (world.getEntities(Enemy.class).size() >= maxEnemies) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastEnemySpawnNanos < enemySpawnInterval) {
            return;
        }

        world.addEntity(createEnemy(gameData));
        lastEnemySpawnNanos = now;
    }

    private Enemy createEnemy(GameData gameData) {
        Enemy enemyShip = new Enemy();
        enemyShip.setPolygonCoordinates(13, 0, 6, 9, -6, 9, -13, 0, -6, -9, 6, -9);
        enemyShip.setX(random.nextDouble(gameData.getDisplayWidth()));
        enemyShip.setY(random.nextDouble(gameData.getDisplayHeight()));
        enemyShip.setRadius(11);
        enemyShip.setLife(2);
        enemyShip.setRotation(random.nextInt(360));
        enemyShip.setFramesUntilDirectionChange(1);
        return enemyShip;
    }

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

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
