package uk.ac.lancaster.scc210.game.bullets.effects;

import org.jsfml.system.Time;
import uk.ac.lancaster.scc210.engine.ecs.Entity;
import uk.ac.lancaster.scc210.game.ecs.component.SpeedComponent;

public class FreezeBulletEffect extends TimedBulletEffect {
    private int oldSpeed;

    public FreezeBulletEffect() {
        super(Time.getSeconds(3));

        oldSpeed = -1;
    }

    @Override
    public void react(Entity entity) {
        super.react(entity);

        if (entity.hasComponent(SpeedComponent.class)) {
            SpeedComponent speedComponent = (SpeedComponent) entity.findComponent(SpeedComponent.class);

            if (oldSpeed == -1) {
                oldSpeed = speedComponent.getSpeed();
            }

            speedComponent.setSpeed(0);

            reactedEntities.add(entity);
        }
    }

    @Override
    public void reset() {
        reactedEntities.forEach(entity -> {
            if (entity.hasComponent(SpeedComponent.class)) {
                System.out.println("Thawing");
                
                SpeedComponent speedComponent = (SpeedComponent) entity.findComponent(SpeedComponent.class);

                speedComponent.setSpeed(oldSpeed);

                System.out.println("New Speed: " + speedComponent.getSpeed());
            }
        });

        reactedEntities.clear();
    }
}
