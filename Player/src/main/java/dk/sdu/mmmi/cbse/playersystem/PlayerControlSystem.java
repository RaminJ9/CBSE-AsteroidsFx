package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class PlayerControlSystem implements IEntityProcessingService {

    private static final double rotationSpeed = 5;
    private static final double acceleration = 0.20;
    private static final double maxSpeed = 4.0;
    private static final double friction = 0.992;
    private static final long shootingCd = 250L;

    private long lastShot;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(Player.class)) {
            if (gameData.getKeys().isDown(GameKeys.left)) {
                player.setRotation(player.getRotation() - rotationSpeed);
            }
            if (gameData.getKeys().isDown(GameKeys.right)) {
                player.setRotation(player.getRotation() + rotationSpeed);
            }
            if (gameData.getKeys().isDown(GameKeys.up)) {
                accelerate(player);
            }
            if (gameData.getKeys().isDown(GameKeys.space)) {
                shootIfReady(player, gameData, world);
            }

            player.setVelocityX(player.getVelocityX() * friction);
            player.setVelocityY(player.getVelocityY() * friction);
            player.setX(player.getX() + player.getVelocityX());
            player.setY(player.getY() + player.getVelocityY());
            wrap(player, gameData);
        }
    }

    private void shootIfReady(Entity player, GameData gameData, World world) {
        long now = System.currentTimeMillis();
        if (lastShot != 0 && now - lastShot < shootingCd) {
            return;
        }
        getBulletSPIs().stream().findFirst().ifPresent(spi -> {
            world.addEntity(spi.createBullet(player, gameData));
            lastShot = now;
        });
    }

    private void accelerate(Entity player) {
        double angle = Math.toRadians(player.getRotation());
        player.setVelocityX(clamp(player.getVelocityX() + Math.cos(angle) * acceleration));
        player.setVelocityY(clamp(player.getVelocityY() + Math.sin(angle) * acceleration));
    }

    private double clamp(double value) {
        return Math.max(-maxSpeed, Math.min(maxSpeed, value));
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
