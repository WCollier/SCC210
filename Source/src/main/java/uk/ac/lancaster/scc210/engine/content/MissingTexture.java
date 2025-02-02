package uk.ac.lancaster.scc210.engine.content;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import uk.ac.lancaster.scc210.engine.resources.ResourceNotFoundException;

/**
 * The alternative for TextureManager. When drawn a P B checker pattern is drawing
 */
class MissingTexture {
    private final Color PURPLE = new Color(255, 0, 255);

    private final Texture texture;

    private final int spriteWidth, spriteHeight;

    private final Image image;

    /**
     * Instantiates a new Missing texture.
     *
     * @param spriteWidth  width at which to draw MissingTexture
     * @param spriteHeight height at which to draw MissingTexture
     * @throws ResourceNotFoundException the resource not found exception
     */
    MissingTexture(final int spriteWidth, final int spriteHeight) throws ResourceNotFoundException {
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        image = new Image();

        image.create(spriteWidth, spriteHeight);

        createImage();

        texture = new Texture();

        try {
            texture.loadFromImage(image);

        } catch (TextureCreationException e) {
            throw new ResourceNotFoundException("Could not create missing texture");
        }
    }

    /**
     * Generate an image in the pattern of:
     * P B
     * B P
     * This is used for for when SpriteManager is unable to find the sprite from the key
     */
    private void createImage() {
        final int halfWidth = spriteWidth / 2;

        final int halfHeight = spriteHeight / 2;

        for (int x = 0; x < spriteWidth; x++) {
            for (int y = 0; y < spriteHeight; y++) {
                if ((x < halfWidth && y < halfHeight) || (x > halfWidth && y > halfWidth)) {
                    image.setPixel(x, y, PURPLE);

                } else {
                    image.setPixel(x, y, Color.BLACK);
                }
            }
        }
    }

    /**
     * Gets texture.
     *
     * @return the texture
     */
    Texture getTexture() {
        return texture;
    }
}
