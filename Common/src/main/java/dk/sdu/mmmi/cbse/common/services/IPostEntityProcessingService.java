package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Per-frame post-processing contract, intended for systems that need the result of all ordinary
 * entity processors, for example collision detection.
 *
 * Precondition: ordinary {@link IEntityProcessingService} implementations have already processed
 * the frame. {@code gameData} and {@code world} must be non-null.
 * Postcondition: global invariants such as collision rules have been applied consistently.
 */
public interface IPostEntityProcessingService {

    void process(GameData gameData, World world);
}
