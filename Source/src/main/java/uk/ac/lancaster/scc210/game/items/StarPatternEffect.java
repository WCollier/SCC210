package uk.ac.lancaster.scc210.game.items;

import uk.ac.lancaster.scc210.engine.ecs.Entity;
import uk.ac.lancaster.scc210.engine.pooling.Pool;
import uk.ac.lancaster.scc210.game.bullets.patterns.StarPattern;
import uk.ac.lancaster.scc210.game.ecs.component.SpaceShipComponent;

public class StarPatternEffect extends FiringPatternEffect {
    public StarPatternEffect(Pool pool) {
        super(pool);
    }

    @Override
    void setPattern(Entity entity) {
        SpaceShipComponent spaceShipComponent = (SpaceShipComponent) entity.findComponent(SpaceShipComponent.class);

        pattern = new StarPattern(pool, entity, spaceShipComponent.getBulletName());
    }
}
