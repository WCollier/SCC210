package uk.ac.lancaster.scc210.engine.ecs;

import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import uk.ac.lancaster.scc210.engine.ecs.system.EntitySystem;
import uk.ac.lancaster.scc210.engine.pooling.Pool;
import uk.ac.lancaster.scc210.engine.service.ServiceProvider;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * World contains all the entities and systems in the game.
 */
public class World {
    private final Set<Entity> entities;

    private final Set<EntitySystem> systems;

    private final ArrayList<Pool> pools;

    private final ServiceProvider serviceProvider;

    /**
     * Instantiates a new World.
     *
     * @param serviceProvider the service provider
     */
    public World(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;

        entities = new HashSet<>();

        systems = new HashSet<>();

        pools = new ArrayList<>();
    }

    /**
     * Add entity to the world. The world will update all the systems that a new entity has been added.
     *
     * @param entity the entity
     */
    public void addEntity(Entity entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);

            systems.forEach(system -> system.entityAdded(entity));
        }
    }

    /**
     * Add entities.
     *
     * @param entities the entities
     */
    public void addEntities(Entity... entities) {
        Set<Entity> entitySet = Set.of(entities);

        this.entities.addAll(entitySet);

        systems.forEach(system -> system.entitiesAdded(entitySet));
    }

    /**
     * Add entities.
     *
     * @param entities the entities
     */
    public void addEntities(Collection<? extends Entity> entities) {
        this.entities.addAll(entities);

        systems.forEach(system -> system.entitiesAdded(entities));
    }

    /**
     * Remove entity.
     *
     * @param entity the entity
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);

        systems.forEach(system -> system.entityRemoved(entity));
    }

    /**
     * Remove entities.
     *
     * @param entities the entities
     */
    public void removeEntities(Collection<? extends Entity> entities) {
        this.entities.removeAll(entities);

        entities.forEach(entity -> systems.forEach(system -> system.entityRemoved(entity)));
    }

    /**
     * Remove if.
     *
     * @param entities the entities
     */
    public void removeIf(Predicate<? super Entity> entities) {
        this.entities.removeIf(entities);
    }

    /**
     * Add a System to the world. It will be iterated upon in the next loop.
     *
     * @param system the system to add
     */
    public void addSystem(EntitySystem system) {
        systems.add(system);
    }

    /**
     * Add a pool to the world. Should be added before any systems are added.
     *
     * @param pool the pool to add
     */
    public void addPool(Pool pool) {
        if (!pools.contains(pool)) {
            pools.add(pool);
        }
    }

    /**
     * Update all the Systems contained in World.
     *
     * @param deltaTime the delta time
     */
    public void update(Time deltaTime) {
        for (EntitySystem system : systems) {
            system.update(deltaTime);
        }
    }

    /**
     * Draw all the Systems contained in World.
     *
     * @param target the target. Usually RenderWindow
     */
    public void draw(RenderTarget target) {
        for (EntitySystem system : systems) {
            system.draw(target);
        }
    }

    /**
     * Create an Entity from a given array of Components.
     *
     * @param components components to add to the new entity.
     * @return the new entity
     */
    public static Entity createEntity(Component... components) {
        return new Entity(Arrays.asList(components));
    }

    /**
     * Given an array of components, find all the entities which have that set
     *
     * @param components the components
     * @return the entities for
     */
    @SafeVarargs
    public final Set<Entity> getEntitiesFor(Class<? extends Component>... components) {
        return entities
                .parallelStream()
                .filter(entity -> entity.getComponents().keySet().containsAll(Arrays.asList(components)))
                .collect(Collectors.toSet());
    }

    /**
     * Given an array of components, find a set of entities which contains at least one of the given components
     *
     * @param components the components
     * @return entities which contain at least component
     */
    @SafeVarargs
    public final Set<Entity> getEntitiesWithAny(Class<? extends Component>... components) {
        Set<Class<? extends Component>> entityComponents = new HashSet<>(Arrays.asList(components));

        return entities
                .stream()
                .filter(entity -> !Collections.disjoint(new HashSet<>(entity.getComponents().keySet()), entityComponents))
                .collect(Collectors.toSet());
    }

    /**
     * From a Class<Pool> return the associate Pool
     *
     * @param klass the Pool to find
     * @return the Pool or null if the Pool can't be found
     */
    public Pool getPool(Class<? extends Pool> klass) {
        return pools
                .parallelStream()
                .filter(pool -> pool.getClass().equals(klass))
                .findFirst()
                .orElse(null);
    }

    /**
     * Clear.
     */
    public void clear() {
        entities.clear();

        systems.clear();
    }

    /**
     * Gets service provider.
     *
     * @return the service provider
     */
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Gets entities.
     *
     * @return the entities
     */
    public Set<Entity> getEntities() {
        return entities;
    }
}
