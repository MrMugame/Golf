import main.GameWindow;

public class Main {
    public static void main(String[] args) {
        GameWindow game = GameWindow.get();
        game.setup();
        game.run();
    }
}