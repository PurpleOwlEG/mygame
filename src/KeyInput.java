import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    private Player player;
    private Game game;
    private boolean up, down, left, right;

    public KeyInput(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W)
            up = true;
        if (key == KeyEvent.VK_S)
            down = true;
        if (key == KeyEvent.VK_A)
            left = true;
        if (key == KeyEvent.VK_D)
            right = true;

        if (key == KeyEvent.VK_R)
            game.resetLevel();
        if (key == KeyEvent.VK_1)
            game.setWeapon(1);
        if (key == KeyEvent.VK_2)
            game.setWeapon(2);
        if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE)
            if (game.getRunning()) {
                game.stop();
            } else {
                game.start();
            }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W)
            up = false;
        if (key == KeyEvent.VK_S)
            down = false;
        if (key == KeyEvent.VK_A)
            left = false;
        if (key == KeyEvent.VK_D)
            right = false;
    }

    public boolean isUpPressed() {
        return up;
    }

    public boolean isDownPressed() {
        return down;
    }

    public boolean isLeftPressed() {
        return left;
    }

    public boolean isRightPressed() {
        return right;
    }
}
