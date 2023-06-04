package game.input;

import game.GameObject;
import game.graphics.StaticGraphic;
import game.physics.ActivePhysicsComponent;
import game.physics.BallPhysics;
import graphics.GameWindow;
import input.MouseListener;
import physics.CollisionMath;
import physics.Vector2D;

import static java.awt.event.MouseEvent.BUTTON1;

public class BallInput extends InputComponent {
    private boolean dragging = false;

    private GameObject drag, arrow;

    public BallInput() {
        arrow = new GameObject("drag", new Vector2D(), new Vector2D(1, 1*(13.0f/14)), 0, 3);
        arrow.add(new StaticGraphic("game/drag_arrow.png"));
        arrow.get(StaticGraphic.class).setAnchor(new Vector2D(0.5f, 0.25f));

        drag = new GameObject("drag", new Vector2D(), new Vector2D(2, 2), 0, 3);
        drag.add(new StaticGraphic("game/drag_1.png"));
        drag.get(StaticGraphic.class).setAnchor(new Vector2D(1, 0.25f));
    }

    @Override
    public void update(float dt) {
        MouseListener listener = MouseListener.get();

        if (dragging) {
            Vector2D dragVector = parent.getTransform().position.sub(listener.getMousePosition());
            drag.getTransform().rotation = dragVector.getAngle() - 0.5f * (float) Math.PI;

            drag.get(StaticGraphic.class).changePath("game/drag_" + (int) Math.max(1, Math.min(Math.floor(dragVector.magnitude() / 0.75), 6)) + ".png");

            arrow.getTransform().rotation = (float) Math.PI + (dragVector.getAngle() - 0.5f * (float) Math.PI);
        }

        if (parent.get(ActivePhysicsComponent.class).velocity.magnitudeSquared() < 0.001f && !dragging && listener.isPressed(BUTTON1) && CollisionMath.pointCircle(parent.getTransform().position , parent.getTransform().size.x / 2 , listener.getMousePosition())) {
            dragging = true;

            drag.getTransform().position = parent.getTransform().position;
            GameWindow.get().getScene().addGameObject(drag);

            arrow.getTransform().position = parent.getTransform().position;
            GameWindow.get().getScene().addGameObject(arrow);

        } else if (dragging && !listener.isPressed(BUTTON1)) {
            BallPhysics physics = (BallPhysics) parent.get(ActivePhysicsComponent.class);
            Vector2D mouseVector = parent.getTransform().position.sub(listener.getMousePosition());
            physics.velocity = mouseVector.normalize().scale(mouseVector.magnitudeSquared());

            GameWindow.get().getScene().removeGameObject(drag);
            GameWindow.get().getScene().removeGameObject(arrow);
            dragging = false;
        }

    }
}
