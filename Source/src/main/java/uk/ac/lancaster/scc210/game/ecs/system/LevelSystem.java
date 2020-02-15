package uk.ac.lancaster.scc210.game.ecs.system;

import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import uk.ac.lancaster.scc210.engine.ecs.Entity;
import uk.ac.lancaster.scc210.engine.ecs.World;
import uk.ac.lancaster.scc210.engine.ecs.system.IterativeSystem;
import uk.ac.lancaster.scc210.game.ecs.component.StationaryComponent;
import uk.ac.lancaster.scc210.game.ecs.component.WaveComponent;
import uk.ac.lancaster.scc210.game.level.Level;
import uk.ac.lancaster.scc210.game.level.LevelStage;

public class LevelSystem extends IterativeSystem {
    private Level level;

    private LevelStage currentStage;

    /**
     * Instantiates a new Iterative system.
     *
     * @param world the world containing entities to use
     */
    public LevelSystem(World world, Level level) {
        super(world);

        this.level = level;

        currentStage = level.getCurrentStage();

        world.addEntities(currentStage.getStationaryEntities());
    }

    @Override
    public void entityRemoved(Entity entity) {
        super.entityRemoved(entity);

        if (entity.hasComponent(WaveComponent.class)) {
            WaveComponent waveComponent = (WaveComponent) entity.findComponent(WaveComponent.class);

            waveComponent.getWave().remove(entity);
        }

        // Remove a stationary entity from the List<Entity> stationaryEntities
        if (entity.hasComponent(StationaryComponent.class)) {
            level.getCurrentStage().removeStationaryEntity(entity);
        }
    }

    @Override
    public void update(Time deltaTime) {
        if (currentStage != null && currentStage.complete()) {
            System.out.println("Completed stage");

            currentStage = level.changeStage();

        } else {
            if (currentStage != null) {
                currentStage.spawn(world, deltaTime);
            }
        }
    }

    @Override
    public void draw(RenderTarget target) {

    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
