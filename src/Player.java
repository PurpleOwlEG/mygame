import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private double x, y;
    private double speed;
    private double hp;
    private double maxHp;
    private BufferedImage playerTexture;
    private int level;
    private int experience;
    private int nextLevelExperience;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.speed = 0.1;
        this.maxHp = 100;
        this.hp = maxHp;
        this.level = 1;
        this.experience = 0;
        this.nextLevelExperience = 100;

        try {
            // Загрузка текстуры игрока
            playerTexture = ImageIO.read(new File("resourse/Wizard.png"));
            // Загрузка текстуры врагов
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(boolean up, boolean down, boolean left, boolean right) {
        double dx = 0;
        double dy = 0;
        if (up)
            dy = -1;
        if (down)
            dy = 1;
        if (left)
            dx = -1;
        if (right)
            dx = 1;

        x += dx * speed;
        y += dy * speed;
    }

    public void render(Graphics2D g2d) {
        if (playerTexture != null) {
            // Отрисовка текстуры игрока
            g2d.drawImage(playerTexture, (int) x, (int) y, 32, 32, null);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Bullet shoot(double mouseX, double mouseY, int weapon) {
        double dx = mouseX - (x + 16);
        double dy = mouseY - (y + 16);
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance != 0) {
            dx /= distance;
            dy /= distance;
        }
        if (weapon == 1) {
            return new Bullet(x + 16, y + 16, dx, dy);
        } else if (weapon == 2) {
            return new Laser(x + 16, y + 16, mouseX, mouseY);
        }
        return null;
    }

    public void resetPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public double getHp() {
        return hp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public double getSpeed() {
        return speed;
    }

    public void gainExperience(double amount) {
        experience += amount;
        if (experience >= nextLevelExperience) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience -= nextLevelExperience;
        nextLevelExperience += 100; // Увеличиваем опыт для следующего уровня
        // Увеличиваем сложность врагов
        Game.increaseEnemyDifficulty();
        // Показываем улучшения для выбора игроком
        Game.showUpgrades(this);
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getNextLevelExperience() {
        return nextLevelExperience;
    }

    public void increaseSpeed(double increase) {
        this.speed *= increase;
    }

    public void increaseHealth(double increase) {
        this.maxHp *= increase;
    }
}
