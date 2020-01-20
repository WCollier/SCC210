package uk.ac.lancaster.scc210.engine.animation;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Clock;
import uk.ac.lancaster.scc210.engine.content.TextureAtlas;
import uk.ac.lancaster.scc210.engine.resources.ResourceNotFoundException;

public class TextureAnimation {
    // 24 FPS is the standard speed for animation
    private final float FPS = 24;

    private Clock animationTimer;

    private Texture[] frames;

    private int currentFrame;

    private float frameTimeRemaning;

    public TextureAnimation(final TextureAtlas textureAtlas, final int startingRow, final int startingColumn, final int numFrames) throws ResourceNotFoundException {
        frames = new Texture[numFrames];

        currentFrame = 0;

        frameTimeRemaning = FPS;

        int currentRow = startingRow;

        int currentColumn = startingColumn;

        for (int i = 0; i < frames.length; i++) {

            frames[i] = textureAtlas.get(currentRow, currentColumn);

            System.out.printf("Column: %d, Row: %d\n", currentColumn, currentRow);

            if (currentColumn < textureAtlas.getColumns() - 1) {
                currentColumn++;

            } else {
                currentRow++;

                currentColumn = 0;
            }
        }
    }

    public Texture getTexture() {
        if (animationTimer == null) {
            // The constructor starts the clock, so initialise it later
            animationTimer = new Clock();
        }

        frameTimeRemaning -= animationTimer.getElapsedTime().asSeconds();

        if (frameTimeRemaning <= 0) {
            currentFrame++;

            // Loop back around to the start
            if (currentFrame >= frames.length) {
                currentFrame = 0;
            }

            frameTimeRemaning = FPS;

            animationTimer.restart();
        }

        return frames[currentFrame];
    }
}
