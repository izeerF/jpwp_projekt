package jpwp;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Firewall {
    private int x;
    private int y;
    private int level;
    private int maxBlocks;
    private int cooldownDuration;
    private int enemiesDestroyed;
    private int cooldownTimer;
    private boolean onCooldown;
    private int totalInvested;

    private StackPane graphic;
    private Rectangle body;
    private Label levelTag;

    public Firewall(int x, int y) {
        this.x = x;
        this.y = y;
        this.level = 1;
        this.maxBlocks = 5;
        this.cooldownDuration = 5;
        this.enemiesDestroyed = 0;
        this.cooldownTimer = 0;
        this.onCooldown = false;
        this.totalInvested = 50;

        this.graphic = new StackPane();
        this.graphic.setPrefSize(45, 45);
        this.graphic.setMinSize(45, 45);
        this.graphic.setMaxSize(45, 45);

        this.body = new Rectangle(45, 45);
        this.body.setFill(Color.RED);
        this.body.setStroke(Color.BLACK);
        this.body.setStrokeType(StrokeType.INSIDE);

        this.levelTag = new Label("1");
        this.levelTag.setTextFill(Color.WHITE);
        this.levelTag.setFont(Font.font("System", FontWeight.BOLD, 12));

        this.graphic.getChildren().addAll(body, levelTag);
    }

    public boolean canUpgrade() {
        return level < 3;
    }

    public int getUpgradeCost() {
        return level * 40;
    }

    public void upgrade() {
        if (canUpgrade()) {
            totalInvested += getUpgradeCost();
            level++;
            maxBlocks += 3;
            cooldownDuration = Math.max(3, cooldownDuration - 1);
            levelTag.setText(String.valueOf(level));
            body.setStrokeWidth(level);
        }
    }

    public int getSellValue() {
        return totalInvested / 2;
    }

    public void destroyEnemy() {
        if (!onCooldown) {
            enemiesDestroyed++;
            if (enemiesDestroyed >= maxBlocks) {
                onCooldown = true;
                cooldownTimer = cooldownDuration;
                body.setFill(Color.GRAY);
            }
        }
    }

    public void updateCooldown() {
        if (onCooldown) {
            cooldownTimer--;
            if (cooldownTimer <= 0) {
                onCooldown = false;
                enemiesDestroyed = 0;
                body.setFill(Color.RED);
            }
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getLevel() { return level; }
    public int getMaxBlocks() { return maxBlocks; }
    public int getCooldownDuration() { return cooldownDuration; }
    public boolean isOnCooldown() { return onCooldown; }
    public Node getGraphic() { return graphic; }
}