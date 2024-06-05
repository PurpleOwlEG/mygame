import java.util.List;
import java.util.ArrayList;

public class Upgrade {
    private String name;
    private String description;
    private Runnable effect;

    public Upgrade(String name, String description, Runnable effect) {
        this.name = name;
        this.description = description;
        this.effect = effect;
    }

    public void apply() {
        effect.run();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static List<Upgrade> getAvailableUpgrades(Player player) {
        List<Upgrade> upgrades = new ArrayList<>();
        upgrades.add(new Upgrade("+1% скорости", "Increase player's speed by 1%", () -> {
            player.increaseSpeed(0.01);
        }));
        upgrades.add(new Upgrade("+20% HP", "Increase player's health by 20%", () -> {
            player.increaseHealth(0.2);
        }));

        // Добавьте другие улучшения
        return upgrades;
    }
}
