import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy {
    private double x, y;
    private double speed;
    private double baseSpeed;
    private Player player;
    private BufferedImage enemyTexture;
    private double experienceReward;

    public Enemy(double x, double y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.baseSpeed = 0.05;
        this.experienceReward = 1000;

        try {
            // Загрузка текстуры врага
            enemyTexture = ImageIO.read(new File("resourse/tile_0122.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance != 0) {
            dx /= distance;
            dy /= distance;
        }
        // Замедление при приближении к игроку
        double speedFactor = Math.max(1.5, distance / 100.0);
        speed = baseSpeed * speedFactor;
        x += dx * speed;
        y += dy * speed;

    }

    public void render(Graphics2D g2d) {
        if (enemyTexture != null) {
            // Отрисовка текстуры врага
            g2d.drawImage(enemyTexture, (int) x, (int) y, 32, 32, null);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getExperienceReward() {
        return experienceReward;
    }
}
