package dk.sdu.mmmi.cbse.common.data;

public class GameData {

    private int displayWidth = 800;
    private int displayHeight = 800;
    private long score;
    private int destroyedAsteroids;
    private final GameKeys keys = new GameKeys();

    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public long getScore() {
        return score;
    }

    public void addScore(long points) {
        score += Math.max(0, points);
    }

    public int getDestroyedAsteroids() {
        return destroyedAsteroids;
    }

    public void incrementDestroyedAsteroids() {
        destroyedAsteroids++;
    }
}
