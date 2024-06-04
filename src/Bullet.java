import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bullet {
    protected double x, y;
    protected double dx, dy;
    private double speed = 0.5;
    private BufferedImage bulletTexture; // Текстура пули

    public Bullet(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;

        try {
            bulletTexture = ImageIO.read(new File("resourse/Bullet (2).png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        x += dx * speed;
        y += dy * speed;
    }

    public void render(Graphics2D g2d) {
        if (bulletTexture != null) {
            // Отрисовка текстуры врага
            g2d.drawImage(bulletTexture, (int) x, (int) y, 7, 7, null);
        }
    }

    public boolean isOffScreen(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
