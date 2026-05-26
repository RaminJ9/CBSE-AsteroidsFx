package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Per-frame update contract for game components.
 *
 * Precondition: {@code gameData} and {@code world} must be non-null and represent the current
 * frame state. Implementations should process only entity types they own or explicitly support.
 * Postcondition: supported entities may have updated position, rotation, velocity, life, or newly
 * created entities; unrelated entities must not be modified.
 */
public interface IEntityProcessingService {

    void process(GameData gameData, World world);
}
