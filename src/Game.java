import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    private boolean running = false;
    private Thread thread;
    private Player player;
    private KeyInput keyInput;
    private MouseInput mouseInput;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<HealthPickup> healthPickups;
    private Random random;
    private int spawnTimer;
    private int healthPickupTimer;
    private int score;
    private int weapon;
    private double width = 800, height = 800;

    public boolean getRunning() {
        return running;
    }

    public Game() {
        Dimension size = new Dimension((int) width, (int) height);
        setPreferredSize(size);
        player = new Player((width / 2 - 16), (height / 2 - 16)); // Центр экрана
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        healthPickups = new ArrayList<>();
        keyInput = new KeyInput(player, this);
        mouseInput = new MouseInput(this);
        this.addKeyListener(keyInput);
        this.addMouseMotionListener(mouseInput);
        this.addMouseListener(mouseInput);
        setFocusable(true);
        random = new Random();
        spawnTimer = 0;
        healthPickupTimer = 0;
        score = 0;
        weapon = 1;
        bullets = Collections.synchronizedList(new ArrayList<>());
        enemies = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (running) {
            update();
            render();
        }
    }

    private void update() {
        player.update(keyInput.isUpPressed(), keyInput.isDownPressed(), keyInput.isLeftPressed(),
                keyInput.isRightPressed());

        // Обновление пуль
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isOffScreen(getWidth(), getHeight())) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);

        // Обновление врагов
        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            enemy.update();
            // Проверка коллизии с игроком
            if (enemy.getX() < player.getX() + 32 && enemy.getX() + 32 > player.getX() &&
                    enemy.getY() < player.getY() + 32 && enemy.getY() + 32 > player.getY()) {
                player.takeDamage(10);
                enemiesToRemove.add(enemy);

            }
            // Проверка коллизии с пулями
            for (Bullet bullet : bullets) {
                if (bullet.getX() > enemy.getX() && bullet.getX() < enemy.getX() + 32 &&
                        bullet.getY() > enemy.getY() && bullet.getY() < enemy.getY() + 32) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    player.gainExperience(enemy.getExperienceReward());
                    score++;
                    break;
                }
            }
        }
        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);
        if (player.getHp() == 0) {
            resetLevel();
        }

        // Обновление хп-квадратиков
        List<HealthPickup> healthPickupsToRemove = new ArrayList<>();
        for (HealthPickup pickup : healthPickups) {
            if (pickup.getX() < player.getX() + 32 && pickup.getX() + 32 > player.getX() &&
                    pickup.getY() < player.getY() + 32 && pickup.getY() + 32 > player.getY()) {
                player.heal(20);
                healthPickupsToRemove.add(pickup);
            }
        }
        healthPickups.removeAll(healthPickupsToRemove);

        // Спавн врагов
        spawnTimer++;
        if ((spawnTimer > 1000 - player.getLevel() * 10) && enemies.size() < (Math.max((player.getLevel() * 2), 10))) {
            spawnEnemy();
            spawnTimer = 0;
        }

        // Спавн хп-квадратиков
        healthPickupTimer++;
        if (healthPickupTimer > 2000 && healthPickups.size() < 5) {
            spawnHealthPickup();
            healthPickupTimer = 0;
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.clearRect(0, 0, getWidth(), getHeight());

        player.render(g2d);
        synchronized (bullets) {
            for (Bullet bullet : bullets) {
                bullet.render(g2d);
            }
        }
        synchronized (enemies) {
            for (Enemy enemy : enemies) {
                // enemy.render(g2d);
                enemy.render(g2d);
            }
        }
        for (HealthPickup pickup : healthPickups) {
            pickup.render(g2d);
        }
        // Отображение HP и счётчика убитых врагов

        g2d.setColor(Color.BLACK);
        g2d.drawString("HP: " + player.getHp(), 10, 20);
        g2d.drawString("Убийств: " + score, 10, 40);
        g2d.drawString("Опыт: " + player.getExperience(), 10, 60);
        g2d.drawString("Уровень: " + player.getLevel(), 10, 80);
        g2d.drawString("Множители:", 10, 120);
        g2d.drawString("Max HP: x" + String.format("%.1f", player.getHpFactor()), 10, 140);
        g2d.drawString("Max Скорость: x" + String.format("%.2f", player.getSpeedFactor()), 10, 160);

        g2d.dispose();
        bs.show();
    }

    private void spawnEnemy() {
        double x, y;
        int side = random.nextInt(4);
        if (side == 0) { // Верхний край
            x = random.nextInt(getWidth());
            y = -32;
        } else if (side == 1) { // Нижний край
            x = random.nextInt(getWidth());
            y = getHeight();
        } else if (side == 2) { // Левый край
            x = -32;
            y = random.nextInt(getHeight());
        } else { // Правый край
            x = getWidth();
            y = random.nextInt(getHeight());
        }
        enemies.add(new Enemy(x, y, player));
    }

    private void spawnHealthPickup() {
        double x = random.nextInt(getWidth() - 32);
        double y = random.nextInt(getHeight() - 32);
        healthPickups.add(new HealthPickup(x, y));
    }

    public void resetLevel() {
        player.resetPosition(width / 2 - 16, height / 2 - 16); // Центр экрана
        bullets.clear();
        enemies.clear();
        healthPickups.clear();
        score = 0;
        player.heal(ABORT);
        player.setLevel(0);
    }

    public void shootBullet(double mouseX, double mouseY) {
        Bullet bullet = player.shoot(mouseX, mouseY);
        if (bullet != null) {
            if (weapon == 1) {
                bullets.add(bullet);

            }
        }
    }

    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }

    public static void showUpgrades(Player player) {
        List<Upgrade> upgrades = Upgrade.getAvailableUpgrades(player);
        String[] options = upgrades.stream().map(Upgrade::getName).toArray(String[]::new);
        int choice = JOptionPane.showOptionDialog(null, "Выберите улучшение:", "Level Up", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice >= 0 && choice < upgrades.size()) {
            upgrades.get(choice).apply();
        }
    }

    // public static void showRestart(Player player) {
        
       
    //     int choice = JOptionPane.showOptionDialog(null, "Choose an upgrade:", "Level Up", JOptionPane.DEFAULT_OPTION,
    //             JOptionPane.ERROR_MESSAGE, null,< "игра пер");
    //     if (choice >= 0 && choice < 1) {
    //        start();
    //     }
    // }

    public static void main(String[] args) {
        JFrame frame = new JFrame("My Game");
        Game game = new Game();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
    }
}
