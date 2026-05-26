package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.Random;

public class EnemyPlugin implements IGamePluginService {

    private final Random random = new Random();
    private Entity enemy;

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy(gameData);
        world.addEntity(enemy);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }

    private Entity createEnemy(GameData gameData) {
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
}
