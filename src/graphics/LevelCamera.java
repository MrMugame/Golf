package graphics;

import game.GameObject;
import input.KeyboardListener;
import physics.Vector2D;
import scenes.levels.Level;

import static java.awt.event.KeyEvent.*;

public class LevelCamera extends Camera {
    private float time = 0;

    private enum CameraState {
        CENTERING,
        FOLLOWING,
        START_ANIMATION,
        ZOOMING_IN,
        ZOOMING_OUT,
        START_BIRDVIEW,
        END_BIRDVIEW
    }
    private CameraState state = CameraState.FOLLOWING;

    private Vector2D startPosition = new Vector2D(), endPosition = new Vector2D();
    private float startZoom, endZoom = 0;

    private float duration = 1000;


    public LevelCamera() {}

    @Override
    public void update(float dt) {
        KeyboardListener listener = KeyboardListener.get();
        if (state != CameraState.ZOOMING_OUT && listener.isPressed(VK_C)) {
            state = CameraState.START_BIRDVIEW;
        } else if (state == CameraState.ZOOMING_OUT && !listener.isPressed(VK_C)) {
            state = CameraState.END_BIRDVIEW;
        }

        GameObject ball = GameWindow.get().getScene().getGameObject("ball");
        if (ball == null) return;
        Vector2D distance = ball.getTransform().position.sub(position);

        time += dt;

        switch (state) {
            case FOLLOWING:
                if (distance.magnitude() > 2f) state = CameraState.CENTERING;
                break;
            case CENTERING:
                Vector2D velocity = distance.scale(2f);
                position = position.add(velocity.scale(dt/1000));

                if (distance.magnitude() < 0.1f) state = CameraState.FOLLOWING;
                break;
            case START_ANIMATION:
                Vector2D diagonal = new Vector2D();
                diagonal.x = ((Level) GameWindow.get().getScene()).getMapWidth();
                diagonal.y = -((Level) GameWindow.get().getScene()).getMapHeight();

                startPosition = diagonal.scale(0.5f);
                startZoom = 1/diagonal.magnitude()*10; // TODO: Proper way of calculating this
                endPosition = ball.getTransform().position;
                endZoom = 1;

                position = startPosition;
                zoom = startZoom;

                // TODO: Don't know if im keeping this
                if (time > 2000)  {
                    state = CameraState.ZOOMING_IN;
                    time = 0;
                }
                break;
            case ZOOMING_IN:
                if (time >= duration) state = CameraState.FOLLOWING;
            case ZOOMING_OUT:
                if (time >= duration) break;

                position = cubicBezier(startPosition, endPosition, time/duration, 0.5f, 0, 0.5f, 1);
                zoom = cubicBezier(startZoom, endZoom, time/duration, 0.5f, 0, 0.5f, 1);

                break;
            case START_BIRDVIEW:
                diagonal = new Vector2D();
                diagonal.x = ((Level) GameWindow.get().getScene()).getMapWidth();
                diagonal.y = -((Level) GameWindow.get().getScene()).getMapHeight();

                endPosition = diagonal.scale(0.5f);
                endZoom = 1/diagonal.magnitude()*10; // TODO: Proper way of calculating this

                startPosition = position;
                startZoom = zoom;

                time = 0;

                state = CameraState.ZOOMING_OUT;
                break;
            case END_BIRDVIEW:
                startPosition = position;
                startZoom = zoom;

                endPosition = ball.getTransform().position;
                endZoom = 1;

                time = 0;

                state = CameraState.ZOOMING_IN;
                break;

        }

    }

    public static Vector2D cubicBezier(Vector2D a, Vector2D b, float t, float p1x, float p1y, float p2x, float p2y) {
        float Bt = cubicBezier(0, 1, t, p1x, p1y, p2x, p2y);
        return a.add(b.sub(a).scale(Bt));
    }

    public static float cubicBezier(float a, float b, float t, float p1x, float p1y, float p2x, float p2y) {
        // p0 und p3 festsetzen ähnlich wie in CSS // https://developer.mozilla.org/en-US/docs/Web/CSS/easing-function#using_the_cubic-bezier_function
        Vector2D p0 = new Vector2D(0, 0);
        Vector2D p1 = new Vector2D(p1x, p1y);
        Vector2D p2 = new Vector2D(p2x, p2y);
        Vector2D p3 = new Vector2D(1, 1);

        // https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Quadratic_B%C3%A9zier_curves
        float Bt = p0.scale((1 - t) * (1 - t) * (1 - t)).add(p1.scale(3 * (1 - t) * (1 - t) * t)).add(p2.scale(3 * (1 - t) * t * t)).add(p3.scale(t * t * t)).y;
        return a + Bt * (b - a);
    }

    public void startAnimation() {
        state = CameraState.START_ANIMATION;
    }
}
