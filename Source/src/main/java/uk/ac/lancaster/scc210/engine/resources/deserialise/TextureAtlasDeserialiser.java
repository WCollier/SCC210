package uk.ac.lancaster.scc210.engine.resources.deserialise;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.lancaster.scc210.engine.content.TextureAtlas;
import uk.ac.lancaster.scc210.engine.resources.ResourceNotFoundException;

/**
 * The type Texture atlas deserialiser.
 */
public class TextureAtlasDeserialiser extends Deserialiser<SerialisedTextureAtlas> {
    /**
     * Instantiates a new Texture atlas deserialiser.
     *
     * @param document the document
     * @throws ResourceNotFoundException the resource not found exception
     */
    public TextureAtlasDeserialiser(Document document) throws ResourceNotFoundException {
        super(document, document.getElementsByTagName("atlas"), "atlas");
    }

    public void deserialise() throws ResourceNotFoundException {
        if (document == null) {
            throw new ResourceNotFoundException("Could not find texture_atlas");
        }

        nodes = document.getElementsByTagName("atlas");

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (foundNode(node)) {
                Element elem = (Element) node;

                String src = elem.getAttribute("src");

                int textureWidth = Integer.parseInt(elem.getAttribute("texture_width"));

                int textureHeight = Integer.parseInt(elem.getAttribute("texture_height"));

                TextureAtlas textureAtlas = new TextureAtlas(src, textureWidth, textureHeight);

                TextureDeserialiser textureDeserialiser = new TextureDeserialiser(elem.getChildNodes(), textureAtlas);

                serialised.add(new SerialisedTextureAtlas(textureAtlas, textureDeserialiser.serialised));
            }
        }

    }
}

