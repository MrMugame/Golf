package scenes.levels;

import game.GameObject;
import game.graphics.StaticGraphic;
import game.input.BallInput;
import game.physics.BallPhysics;
import game.physics.Wall;
import graphics.DebugCamera;
import graphics.LevelCamera;
import physics.Vector2D;
import scenes.Scene;

public class Level extends Scene {

    private final LevelLoader loader;

    private GameObject background;

    public Level(String path) {
        super(new LevelCamera());
        loader = new LevelLoader(path);
    }

    @Override
    public void init() {
        try {
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        background = loader.renderBackground();
        addGameObject(background);

        GameObject walls = new GameObject("walls", new Vector2D(0, 0), new Vector2D(loader.getMap().width, loader.getMap().height), 0);
        walls.add(new Wall(loader.getMap().track));
        addGameObject(walls);


        GameObject g1 = new GameObject("ball", new Vector2D(3f, -2.5f), new Vector2D(0.5f, 0.5f), 5);
        g1.add(new StaticGraphic("game/ball.png"));
        g1.add(new BallPhysics(10f));
        g1.add(new BallInput());
        addGameObject(g1);
    }

    public float getMapWidth() {
        return loader.getMap().width;
    }

    public float getMapHeight() {
        return loader.getMap().height;
    }
}