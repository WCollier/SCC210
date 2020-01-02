package uk.ac.lancaster.scc210.ecs.system;

import org.jsfml.graphics.RenderTarget;

public interface EntitySystem {
    void entityAdded();

    void update();

    void draw(RenderTarget target);
}
