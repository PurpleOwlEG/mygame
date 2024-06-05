import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HealthPickup {
    private double x, y;
    private BufferedImage healthPickupTexture;

    public HealthPickup(double x, double y) {
        this.x = x;
        this.y = y;
        try {
            // Загрузка текстуры зельки
            healthPickupTexture = ImageIO.read(new File("resourse/tile_0115.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics2D g2d) {
        if (healthPickupTexture != null) {
            // Отрисовка текстуры зельки
            g2d.drawImage(healthPickupTexture, (int) x, (int) y, 32, 32, null);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
