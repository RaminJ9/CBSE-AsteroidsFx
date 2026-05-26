package dk.sdu.mmmi.cbse.common.data;

public class GameKeys {

    public static final int up = 0;
    public static final int left = 1;
    public static final int right = 2;
    public static final int space = 3;

    private static final int nrKeys = 4;
    private final boolean[] keys = new boolean[nrKeys];
    private final boolean[] previousKeys = new boolean[nrKeys];

    public void update() {
        System.arraycopy(keys, 0, previousKeys, 0, nrKeys);
    }

    public void setKey(int key, boolean down) {
        if (key >= 0 && key < nrKeys) {
            keys[key] = down;
        }
    }

    public boolean isDown(int key) {
        return key >= 0 && key < nrKeys && keys[key];
    }

    public boolean isPressed(int key) {
        return key >= 0 && key < nrKeys && keys[key] && !previousKeys[key];
    }
}
