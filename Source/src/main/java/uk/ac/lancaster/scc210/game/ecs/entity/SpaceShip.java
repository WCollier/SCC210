package uk.ac.lancaster.scc210.game.ecs.entity;

import org.jsfml.graphics.Sprite;
import uk.ac.lancaster.scc210.engine.content.TextureAnimationManager;
import uk.ac.lancaster.scc210.engine.ecs.Entity;
import uk.ac.lancaster.scc210.engine.ecs.World;
import uk.ac.lancaster.scc210.game.ecs.component.*;

public class SpaceShip {
    private final TextureAnimationManager animationManager;

    private final String animation;

    private final int speed;

    public SpaceShip(TextureAnimationManager animationManager, String animation, int speed) {
        this.animationManager = animationManager;
        this.animation = animation;
        this.speed = speed;
    }

    public Entity createEntity() {
        final AnimationComponent animationComponent = new AnimationComponent(animationManager.get(animation));

        final SpriteComponent spriteComponent = new SpriteComponent(new Sprite(animationComponent.getTextureAnimation().getTexture()));

        final SpeedComponent speedComponent = new SpeedComponent(speed);

        final RotationComponent rotationComponent = new RotationComponent(2f);

        final SpaceShipComponent spaceShipComponent = new SpaceShipComponent();

        return World.createEntity(animationComponent, spriteComponent, speedComponent, rotationComponent, spaceShipComponent);
    }
}
