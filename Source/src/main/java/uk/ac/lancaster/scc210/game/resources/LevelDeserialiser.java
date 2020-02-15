package uk.ac.lancaster.scc210.game.resources;

import org.jsfml.system.Vector2f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.lancaster.scc210.engine.content.ShaderManager;
import uk.ac.lancaster.scc210.engine.content.TextureManager;
import uk.ac.lancaster.scc210.engine.ecs.Entity;
import uk.ac.lancaster.scc210.engine.prototypes.Prototype;
import uk.ac.lancaster.scc210.engine.resources.ResourceNotFoundException;
import uk.ac.lancaster.scc210.engine.resources.deserialise.Deserialiser;
import uk.ac.lancaster.scc210.game.content.SpaceShipPrototypeManager;
import uk.ac.lancaster.scc210.game.ecs.component.*;
import uk.ac.lancaster.scc210.game.level.Level;
import uk.ac.lancaster.scc210.game.level.LevelStage;
import uk.ac.lancaster.scc210.game.level.LevelWave;
import uk.ac.lancaster.scc210.game.prototypes.AsteroidPrototype;
import uk.ac.lancaster.scc210.game.waves.SineWave;
import uk.ac.lancaster.scc210.game.waves.StraightLineWave;
import uk.ac.lancaster.scc210.game.waves.Wave;

import java.util.ArrayList;
import java.util.List;

public class LevelDeserialiser extends Deserialiser<Level> {
    private final String STAGE_TAG = "stage";

    private final String WAVE_TAG = "wave";

    private final String STATIONARY_TAG = "stationary";

    private final String SPACE_SHIP = "spaceship";

    private final String ASTEROID = "asteroid";

    private final SpaceShipPrototypeManager spaceShipManager;

    private final AsteroidPrototype asteroidPrototype;

    /**
     * Instantiates a new Deserialiser.
     *
     * @param document the xml document
     * @throws ResourceNotFoundException if the resource cannot be created or found
     */
    public LevelDeserialiser(SpaceShipPrototypeManager spaceShipManager, TextureManager textureManager, ShaderManager shaderManager, Document document) throws ResourceNotFoundException {
        super(document, "level");

        this.spaceShipManager = spaceShipManager;

        asteroidPrototype = new AsteroidPrototype(textureManager, shaderManager);

        deserialise();
    }

    @Override
    protected void deserialise() throws ResourceNotFoundException {
        if (spaceShipManager == null) {
            return;
        }

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (foundNode(node)) {
                serialised.add(new Level(deserialiseStage(node.getChildNodes())));
            }
        }
    }

    private List<LevelStage> deserialiseStage(NodeList nodes) throws ResourceNotFoundException {
        List<LevelStage> stages = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (foundNode(node, STAGE_TAG)) {
                NodeList childNodes = node.getChildNodes();

                stages.add(new LevelStage(deserialiseWave(childNodes), deserialiseStationary(childNodes)));
            }
        }

        return stages;
    }

    private List<LevelWave> deserialiseWave(NodeList nodes) throws ResourceNotFoundException {
        List<LevelWave> waves = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (foundNode(node, WAVE_TAG)) {
                Element elem = (Element) node;

                float originX = Float.parseFloat(elem.getAttribute("start_x"));

                float originY = Float.parseFloat(elem.getAttribute("start_y"));

                float destinationX = Float.parseFloat(elem.getAttribute("end_x"));

                float destinationY = Float.parseFloat(elem.getAttribute("end_y"));

                int numShips = Integer.parseInt(elem.getAttribute("num_ships"));

                String entityType = elem.getAttribute("entity_type");

                Prototype prototype = null;

                if (entityType.equals(SPACE_SHIP)) {
                    prototype = spaceShipManager.get(elem.getAttribute("ship_name"));

                } else if (entityType.equals(ASTEROID)) {
                    prototype = asteroidPrototype;
                }

                Vector2f origin = new Vector2f(originX, originY);

                Vector2f destination = new Vector2f(destinationX, destinationY);

                Wave wave = deserialiseWaveName(elem.getAttribute("type"), origin, destination);

                if (prototype != null) {
                    waves.add(new LevelWave(wave, origin, destination, numShips, prototype));
                }
            }
        }

        return waves;
    }

    private List<Entity> deserialiseStationary(NodeList nodes) {
        List<Entity> entities = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (foundNode(node, STATIONARY_TAG)) {
                Element elem = (Element) node;

                float posX = Float.parseFloat(elem.getAttribute("pos_x"));

                float posY = Float.parseFloat(elem.getAttribute("pos_y"));

                Vector2f pos = new Vector2f(posX, posY);

                String entityType = elem.getAttribute("entity_type");

                Entity entity = null;

                if (entityType.equals(SPACE_SHIP)) {
                    entity = deserialiseStationarySpaceShip(elem, pos);

                } else if (entityType.equals(ASTEROID)) {
                    entity = deserialiseStationaryAsteroid(pos);
                }

                if (entity != null) {
                    if (entity.hasComponent(SpeedComponent.class)) {
                        entity.removeComponent(SpeedComponent.class);
                    }
                }

                entities.add(entity);
            }
        }

        return entities;
    }

    private Entity deserialiseStationarySpaceShip(Element elem, Vector2f position) {
        String shipName = elem.getAttribute("ship_name");

        Entity spaceShip = spaceShipManager.get(shipName).create();

        spaceShip.addComponent(new StationaryComponent());

        spaceShip.addComponent(new EnemyComponent());

        SpriteComponent spriteComponent = (SpriteComponent) spaceShip.findComponent(SpriteComponent.class);

        spriteComponent.getSprite().setPosition(position);

        return spaceShip;
    }

    private Entity deserialiseStationaryAsteroid(Vector2f pos) {
        Entity asteroid = asteroidPrototype.create();

        AsteroidComponent asteroidComponent = (AsteroidComponent) asteroid.findComponent(AsteroidComponent.class);

        asteroid.addComponent(new StationaryComponent());

        asteroid.addComponent(new EnemyComponent());

        asteroidComponent.getCircle().setPosition(pos);

        return asteroid;
    }

    private Wave deserialiseWaveName(String wave, Vector2f origin, Vector2f destination) throws ResourceNotFoundException {
        switch (wave) {
            case "straight":
                return new StraightLineWave(origin, destination);

            case "sine":
                return new SineWave(origin, destination);

            default:
                throw new ResourceNotFoundException(String.format("Could not find given wave: %s.", wave));
        }
    }
}
