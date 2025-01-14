package graphics;

import game.GameObject;
import game.Transform;
import game.graphics.GraphicComponent;
import physics.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class LayerRenderer {
    public final int Zindex;

    public ArrayList<GraphicComponent> components = new ArrayList<>();

    public LayerRenderer(int z) {
        Zindex = z;
    }

    public void add(GameObject go) {
        GraphicComponent comp = go.get(GraphicComponent.class);
        components.add(comp);
    }

    public void remove(GameObject go) {
        GraphicComponent comp = go.get(GraphicComponent.class);
        components.remove(comp);
    }

    public void render(Graphics2D g) {
        for (GraphicComponent component : components) {
            Transform transform = component.parent.getTransform();
            Vector2D size = Transform.toScreenSize(transform.size);

            Vector2D pos = Transform.toScreenPosition(transform.position.sub(component.getAnchor()));

            // Fälle separieren aufgrund von Performance
            if (transform.rotation == 0) {
                g.drawImage(component.getTexture(), (int) pos.x, (int) pos.y, (int) size.x, (int) size.y, null);
            } else {
                AffineTransform backup = g.getTransform();

                Vector2D origin = Transform.toScreenPosition(transform.position);
                g.rotate(transform.rotation, (int) origin.x, (int) origin.y);

                g.drawImage(component.getTexture(), (int) pos.x, (int) pos.y, (int) size.x, (int) size.y, null);

                g.setTransform(backup);
            }
        }
    }
}
