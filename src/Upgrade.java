import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

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
        upgrades.add(new Upgrade("Increase Speed", "Increase player's speed by 10%", () -> {
            player.increaseSpeed(1.1);
        }));
        upgrades.add(new Upgrade("Increase Health", "Increase player's health by 20%", () -> {
            player.increaseHealth(1.2);
        }));
        // Добавьте другие улучшения
        return upgrades;
    }
}
