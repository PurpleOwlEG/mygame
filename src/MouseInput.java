import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {
    private static int mouseX;
    private static int mouseY;
    private boolean isShooting;
    private static Game game;

    public MouseInput(Game game) {
        MouseInput.game = game;
        this.isShooting = false;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isShooting = true;
            game.shootBullet(mouseX, mouseY);
        }
    }

    public static void shooting() {

        game.shootBullet(mouseX, mouseY);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isShooting = false;
        }

    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isShooting() {
        return isShooting;
    }
}
