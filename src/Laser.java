import java.awt.Color;
import java.awt.Graphics2D;

public class Laser extends Bullet {
    private double targetX, targetY;
    private int lifeTime;

    public void setLifeTime(int newLifeTime) {
        lifeTime = newLifeTime;
    }

    public Laser(double x, double y, double targetX, double targetY) {
        super(x, y, 0, 0);
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void update() {
        // Лазер не двигается, он мгновенно уничтожает врагов на своем пути
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.drawLine((int) x, (int) y, (int) targetX, (int) targetY);
    }

    @Override
    public boolean isOffScreen(int width, int height) {
        if (lifeTime < 100) {
            lifeTime++;
            return false; // Лазер не исчезает сразу после выстрела

        } else {
            return true;
        }

    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }
}
