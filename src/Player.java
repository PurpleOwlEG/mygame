import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private double x, y;
    private double speed;
    private int hp;
    private double baseSpeed;
    private int maxHp;
    private BufferedImage playerTexture;
    private int level;
    private int experience;
    private int nextLevelExperience;
    private double hpFactor;
    private double speedFactor;
    private double baseHp;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.speed = 0.1;
        this.baseSpeed = 0.1;
        this.maxHp = 100;
        this.hp = maxHp;
        this.baseHp = 100;
        this.level = 1;
        this.experience = 0;
        this.nextLevelExperience = 100;
        this.hpFactor = 1;
        this.speedFactor = 1;

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

    public Bullet shoot(double mouseX, double mouseY) {
        double dx = mouseX - (x + 16);
        double dy = mouseY - (y + 16);
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance != 0) {
            dx /= distance;
            dy /= distance;
        }
        return new Bullet(x + 16, y + 16, dx, dy);
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

    public int getMaxHp() {
        return maxHp;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHpFactor() {
        return hpFactor;
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    public void setHpFactor(double hpFactor) {
        this.hpFactor = hpFactor;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
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
        nextLevelExperience += 50; // Увеличиваем опыт для следующего уровня
        // Показываем улучшения для выбора игроком
        Game.showUpgrades(this);
    }

    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getExperience() {
        return experience;
    }

    public int getNextLevelExperience() {
        return nextLevelExperience;
    }

    public void increaseSpeed(double increase) {
        this.speedFactor += increase;
        this.speed = baseSpeed * speedFactor;
    }

    public void increaseHealth(double increase) {
        this.hpFactor += increase;
        this.maxHp = (int) (baseHp * hpFactor);
    }
}
