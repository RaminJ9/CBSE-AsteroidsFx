package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {

    private Entity player;

    @Override
    public void start(GameData gameData, World world) {
        player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }

    private Entity createPlayerShip(GameData gameData) {
        Player playerShip = new Player();
        playerShip.setPolygonCoordinates(-8, -7, 14, 0, -8, 7, -4, 0);
        playerShip.setX(gameData.getDisplayWidth() / 2.0);
        playerShip.setY(gameData.getDisplayHeight() / 2.0);
        playerShip.setRadius(10);
        playerShip.setLife(3);
        return playerShip;
    }
}
