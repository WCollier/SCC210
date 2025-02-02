package uk.ac.lancaster.scc210.game.content;

import uk.ac.lancaster.scc210.engine.content.ContentManager;
import uk.ac.lancaster.scc210.engine.pooling.Pool;
import uk.ac.lancaster.scc210.engine.service.ServiceProvider;
import uk.ac.lancaster.scc210.game.prototypes.SpaceShipPrototype;
import uk.ac.lancaster.scc210.game.prototypes.alternatives.AlterantiveSpaceShipPrototype;
import uk.ac.lancaster.scc210.game.resources.SerialisedSpaceShip;

import java.util.List;

/**
 * A content manager for storing Space Ships.
 */
public class SpaceShipPrototypeManager extends ContentManager<SpaceShipPrototype> {
    /**
     * Instantiates a new Space ship manager.
     * Places the given serialisedSpaceShips, place the Space Ships into the manager.
     *
     * @param serviceProvider      the service provider is used to access resources
     * @param pool                 the pool
     * @param serialisedSpaceShips the XML document which contains Space Ships
     */
    public SpaceShipPrototypeManager(ServiceProvider serviceProvider, Pool pool, List<SerialisedSpaceShip> serialisedSpaceShips) {
        super(null);

        // Set outside of super so 'this' can be accessed
        alternative = new AlterantiveSpaceShipPrototype(serviceProvider, this, pool).getAlternative();

        for (SerialisedSpaceShip serialisedSpaceShip : serialisedSpaceShips) {
            SpaceShipPrototype spaceShip = new SpaceShipPrototype(serviceProvider, this, pool, serialisedSpaceShip);

            put(serialisedSpaceShip.getName(), spaceShip);
        }
    }
}
