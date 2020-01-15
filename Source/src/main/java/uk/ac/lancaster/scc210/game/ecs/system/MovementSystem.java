package uk.ac.lancaster.scc210.game.ecs.system;

import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.Keyboard;
import uk.ac.lancaster.scc210.engine.ecs.Entity;
import uk.ac.lancaster.scc210.engine.ecs.World;
import uk.ac.lancaster.scc210.engine.ecs.system.IterativeSystem;
import uk.ac.lancaster.scc210.game.ecs.component.SpeedComponent;
import uk.ac.lancaster.scc210.game.ecs.component.SpriteComponent;

public class MovementSystem extends IterativeSystem {
    public MovementSystem(World world) {
        super(world, SpriteComponent.class, SpeedComponent.class);
    }

    @Override
    public void update() {
        for (Entity entity : entities) {
            SpriteComponent spriteComponent = (SpriteComponent) entity.findComponent(SpriteComponent.class);

            SpeedComponent speedComponent = (SpeedComponent) entity.findComponent(SpeedComponent.class);

            int moveY = 0;

            if (Keyboard.isKeyPressed(Keyboard.Key.W)) {
                moveY = -speedComponent.getSpeed();

            } else if (Keyboard.isKeyPressed(Keyboard.Key.S)) {
                moveY = speedComponent.getSpeed();
            }

            spriteComponent.getSprite().move(0, moveY);
        }
    }

    @Override
    public void draw(RenderTarget target) {

    }
}
