package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Lifecycle contract for components that add or remove entities from the game world.
 *
 * Precondition for both operations: {@code gameData} and {@code world} must be non-null.
 * Postcondition for {@link #start(GameData, World)}: the component's initial entities, if any,
 * have been registered in {@code world}.
 * Postcondition for {@link #stop(GameData, World)}: entities owned by the component have been
 * removed from {@code world} without modifying unrelated components' entities.
 */
public interface IGamePluginService {

    void start(GameData gameData, World world);

    void stop(GameData gameData, World world);
}
