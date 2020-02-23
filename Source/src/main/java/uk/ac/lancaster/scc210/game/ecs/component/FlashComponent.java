package uk.ac.lancaster.scc210.game.ecs.component;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.Shader;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import uk.ac.lancaster.scc210.engine.ecs.Component;

public class FlashComponent implements Component {
    private final Time FLASH_LENGTH = Time.getSeconds(0.3f);

    private final RenderStates textureState;

    private final RenderStates flashState;

    private Time elapsedTime;

    private RenderStates currentState;

    private Shader flashShader;

    private boolean flashing;

    public FlashComponent(Sprite sprite, Shader flashShader) {
        this.flashShader = flashShader;

        textureState = new RenderStates(sprite.getTexture());

        setShaderWhite();

        flashState = new RenderStates(flashShader);

        currentState = textureState;

        flashing = false;

        elapsedTime = Time.ZERO;
    }

    public FlashComponent(CircleShape circleShape, Shader flashShader) {
        textureState = new RenderStates(circleShape.getTexture());

        flashState = new RenderStates(flashShader);

        currentState = textureState;

        flashing = false;

        elapsedTime = Time.ZERO;
    }

    public void updateRenderState(Time deltaTime) {
        if (flashing) {
            elapsedTime = Time.add(elapsedTime, deltaTime);

            // Sprite has been flashing for more than FLASH_LENGTH seconds
            if (elapsedTime.asSeconds() >= FLASH_LENGTH.asSeconds()) {
                flashing = false;

                elapsedTime = Time.ZERO;

                currentState = textureState;
            }

        } else {
            currentState = textureState;
        }
    }

    public void flash(Time deltaTime) {
        // Don't bother to flash if shaders aren't available
        if (flashing || !Shader.isAvailable()) {
            return;
        }

        currentState = flashState;

        flashing = true;

        elapsedTime = deltaTime;
    }

    public void resetToTexture() {
        if (flashing) {
            currentState = textureState;

            // Reset the timer
            elapsedTime = Time.ZERO;
        }
    }

    public RenderStates getCurrentState() {
        return currentState;
    }

    private void setShaderWhite() {
        flashShader.setParameter("result_colour", 1, 1, 1, 1);
    }

    public void setShaderRed() {
        flashShader.setParameter("result_colour", 1, 0, 0, 1);
    }
}
