package uk.ac.lancaster.scc210.engine.collision;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Shape;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2f;

/**
 * The type Orientated box.
 */
public class OrientatedBox {
    //private static Vector2f[] axes = { Vector2f.ZERO, Vector2f.ZERO, Vector2f.ZERO, Vector2f.ZERO };

    private static final float[][] axes = { new float[2], new float[2], new float[2], new float[3] };

    private static final int POINTS = 4;

    private final Vector2f[] points;

    private Sprite sprite;

    private Shape shape;

    private FloatRect globalBounds;

    /**
     * Describes a box which accounts for the transformation of the sprite. By default, SFML rectangles do not store the
     * transformation of the rotation. This makes collision checking difficult to implement on transformed rectangles.
     * This fixes that.
     *
     * @param sprite the sprite to perform collisions up
     */
    public OrientatedBox(Sprite sprite) {
        this.sprite = sprite;

        points = new Vector2f[POINTS];

        globalBounds = sprite.getGlobalBounds();

        calculatePoints();
    }

    /**
     * Describes a box which accounts for the transformation of the sprite. By default, SFML rectangles do not store the
     * transformation of the rotation. This makes collision checking difficult to implement on transformed rectangles.
     * This fixes that.
     *
     * @param shape the shape to perform collisions up
     */
    public OrientatedBox(Shape shape) {
        this.shape = shape;

        points = new Vector2f[POINTS];
    }

    /**
     * Calculate points.
     */
    public void calculatePoints() {
        /*
         * Unfortunately JSFML does not provide an abstraction where Sprite can be considered a Shape (despite storing a RectangleShape).
         * As such, we have to handle to handle two separate cases of Sprite and Shape. This is unfortunate...
         */
        Transform trans = null;

        if (sprite != null) {
            if (sprite.getGlobalBounds() == globalBounds) {
                return;

            } else {
                globalBounds = sprite.getGlobalBounds();

                trans = sprite.getTransform();
            }
        }

        if (shape != null) {
            if (shape.getGlobalBounds() == globalBounds) {
                return;

            } else {
                globalBounds = shape.getGlobalBounds();

                trans = shape.getTransform();
            }
        }

        if (globalBounds != null && trans != null) {
            points[0] = trans.transformPoint(0.f, 0.f);

            points[1] = trans.transformPoint(globalBounds.width, 0.f);

            points[2] = trans.transformPoint(globalBounds.width, globalBounds.height);

            points[3] = trans.transformPoint(0.f, globalBounds.height);
        }
    }

    private float[] projectOntoAxis(float[] axis) {
        // Perform dot product on every axis
        float min = (points[0].x * axis[0] + points[0].y * axis[1]);

        float max = min;

        for (int j = 1; j < POINTS; j++) {
            float projection = (points[j].x * axis[0] + points[j].y * axis[1]);

            if (projection < min) {
                min = projection;
            }

            if (projection > max) {
                max = projection;
            }
        }

        return new float[] {min, max};
    }

    /**
     * Performs a Separate Axis Theorem collision check between two boxes (including rotation and scaling).
     * This method should be used to account for rotation when performing collision checks between two entities
     * <p>
     * See: https://github.com/SFML/SFML/wiki/Source%3A-Simple-Collision-Detection-for-SFML-2 for the exact implementation
     * of the theorem, this code adapts it for Java and changes the abstraction in an attempt to improve performance.
     *
     * @param left  The first of the bounding boxes to check against
     * @param right The second of the bounding boxes to check against
     * @return if the two boxes are colliding or not
     */
    public static boolean areColliding(OrientatedBox left, OrientatedBox right) {
        left.calculatePoints();

        right.calculatePoints();

        axes[0][0] = left.points[1].x - left.points[0].x;
        axes[0][1] = left.points[1].y - left.points[0].y;

        axes[1][0] = left.points[1].x - left.points[2].x;
        axes[1][1] = left.points[1].y - left.points[2].y;

        axes[2][0] = right.points[0].x - right.points[3].x;
        axes[2][1] = right.points[0].y - right.points[3].y;

        axes[3][0] = right.points[0].x - right.points[1].x;
        axes[3][1] = right.points[0].y - right.points[1].y;

        /*
        axes[0] = new Vector2f(left.points[1].x - left.points[0].x, left.points[1].y - left.points[0].y);

        axes[1] = new Vector2f(left.points[1].x - left.points[2].x, left.points[1].y - left.points[2].y);

        axes[2] = new Vector2f(right.points[0].x - right.points[3].x, right.points[0].y - right.points[3].y);

        axes[3] = new Vector2f(right.points[0].x - right.points[1].x, right.points[0].y - right.points[1].y);
         */

        /*
        Vector2f[] axes = {
                new Vector2f(left.points[1].x - left.points[0].x, left.points[1].y - left.points[0].y),
                new Vector2f(left.points[1].x - left.points[2].x, left.points[1].y - left.points[2].y),
                new Vector2f(right.points[0].x - right.points[3].x, right.points[0].y - right.points[3].y),
                new Vector2f(right.points[0].x - right.points[1].x, right.points[0].y - right.points[1].y)
        };

         */

        for (int i = 0; i < POINTS; i++) {
            float[] leftBoxProjections = left.projectOntoAxis(axes[i]);

            float[] rightBoxProjections = right.projectOntoAxis(axes[i]);

            if (!((rightBoxProjections[0] <= leftBoxProjections[1]) && (rightBoxProjections[1] >= leftBoxProjections[0]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get points vector 2 f [ ].
     *
     * @return the vector 2 f [ ]
     */
    public Vector2f[] getPoints() {
        return points;
    }
}