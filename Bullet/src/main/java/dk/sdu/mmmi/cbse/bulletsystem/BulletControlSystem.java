package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    private static final double bulletSpeed = 6.5;
    private static final long bulletLifetime = 3000L;

    // Updates bullet position every frame, and checks lifespan.
    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Bullet.class)) {
            Bullet bullet = (Bullet) entity;
            bullet.setX(bullet.getX() + bullet.getVelocityX());
            bullet.setY(bullet.getY() + bullet.getVelocityY());
            bullet.addDistanceTravelled(Math.hypot(bullet.getVelocityX(), bullet.getVelocityY()));

            boolean expired = System.currentTimeMillis() - bullet.getCreateAtMilli() >= bulletLifetime;
            boolean outsideScreen = bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth()
                    || bullet.getY() < 0 || bullet.getY() > gameData.getDisplayHeight();

            if (expired || outsideScreen) {
                world.removeEntity(bullet);
            }
        }
    }

    // Creates new bullet entity in game.
    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet bullet = new Bullet();
        bullet.setPolygonCoordinates(3, -2, 3, 2, -3, 2, -3, -2);
        double angle = Math.toRadians(shooter.getRotation());
        bullet.setX(shooter.getX() + Math.cos(angle) * (shooter.getRadius() + 5));
        bullet.setY(shooter.getY() + Math.sin(angle) * (shooter.getRadius() + 5));
        bullet.setRotation(shooter.getRotation());
        bullet.setVelocityX(Math.cos(angle) * bulletSpeed);
        bullet.setVelocityY(Math.sin(angle) * bulletSpeed);
        bullet.setRadius(3);
        bullet.setOwnerId(shooter.getID());
        bullet.setOwnerType(shooter.getClass().getName());
        bullet.setDamage(1);
        return bullet;
    }
}
