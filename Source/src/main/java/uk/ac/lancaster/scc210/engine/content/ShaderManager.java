package uk.ac.lancaster.scc210.engine.content;

import org.jsfml.graphics.Shader;
import uk.ac.lancaster.scc210.engine.resources.ResourceLoader;
import uk.ac.lancaster.scc210.engine.resources.ResourceNotFoundException;
import uk.ac.lancaster.scc210.engine.resources.ShaderAdapter;

/**
 * The type Shader manager.
 */
public class ShaderManager extends ContentManager<Shader> {
    /**
     * Instantiates a new ShaderManager.
     *
     * @throws ResourceNotFoundException the resource not found exception
     */
    public ShaderManager() throws ResourceNotFoundException {
        super(new Shader());

        Shader whiteFlash = new Shader();

        ResourceLoader.loadFromStream(new ShaderAdapter(whiteFlash, Shader.Type.FRAGMENT), "flash.frag");

        put("flash", whiteFlash);
    }
}
