package core.game;

public class FlipACoin {
    public final boolean getResult() {
        return (int) (Math.random() * 2) == 0;
    }
}
