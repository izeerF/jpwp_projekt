package jpwp;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private int idleTimer;
    private boolean onCooldown;
    private int totalInvested;

    private StackPane graphic;
    private Rectangle body;
    private Label topLabel;
    private Label centerLabel;
    private Rectangle barFill;

    public Firewall(int x, int y) {
        this.x = x;
        this.y = y;
        this.level = 1;
        this.maxBlocks = 5;
        this.cooldownDuration = 5;
        this.enemiesDestroyed = 0;
        this.cooldownTimer = 0;
        this.idleTimer = 0;
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
        this.body.setStrokeWidth(1.5);

        this.topLabel = new Label("poz.1");
        this.topLabel.setTextFill(Color.WHITE);
        this.topLabel.setFont(Font.font("System", FontWeight.BOLD, 9));

        this.centerLabel = new Label("5/5");
        this.centerLabel.setTextFill(Color.WHITE);
        this.centerLabel.setFont(Font.font("System", FontWeight.BOLD, 11));

        Rectangle barBg = new Rectangle(35, 4, Color.rgb(0, 0, 0, 0.45));
        this.barFill = new Rectangle(35, 4, Color.LIMEGREEN);

        StackPane barContainer = new StackPane(barBg, barFill);
        barContainer.setAlignment(Pos.CENTER_LEFT);
        barContainer.setMaxWidth(35);

        VBox overlay = new VBox(1, topLabel, centerLabel, barContainer);
        overlay.setAlignment(Pos.CENTER);

        this.graphic.getChildren().addAll(body, overlay);
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
            topLabel.setText("poz." + level);
            body.setStrokeWidth(level + 0.5);
            refreshVisuals();
        }
    }

    public int getSellValue() {
        return totalInvested / 2;
    }

    public void destroyEnemy() {
        if (!onCooldown) {
            enemiesDestroyed++;
            idleTimer = 0; // reset czasu bezczynnosci po trafieniu

            if (enemiesDestroyed >= maxBlocks) {
                onCooldown = true;
                cooldownTimer = cooldownDuration;
            }
            refreshVisuals();
        }
    }

    public void updateCooldown() {
        if (onCooldown) {
            cooldownTimer--;
            if (cooldownTimer <= 0) {
                onCooldown = false;
                enemiesDestroyed = 0;
                idleTimer = 0;
            }
            refreshVisuals();
        } else if (enemiesDestroyed > 0) {
            idleTimer++;
            if (idleTimer >= 5) {
                enemiesDestroyed = 0;
                idleTimer = 0;
                refreshVisuals();
            }
        }
    }

    private void refreshVisuals() {
        if (onCooldown) {
            body.setFill(Color.DIMGRAY);
            centerLabel.setText(cooldownTimer + "s");
            centerLabel.setTextFill(Color.GOLD);
            double progress = (double) (cooldownDuration - cooldownTimer) / cooldownDuration;
            barFill.setWidth(Math.max(2, 35.0 * progress));
            barFill.setFill(Color.ORANGE);
        } else {
            body.setFill(Color.RED);
            int remaining = maxBlocks - enemiesDestroyed;
            centerLabel.setText(remaining + "/" + maxBlocks);
            centerLabel.setTextFill(Color.WHITE);
            double ratio = (double) remaining / maxBlocks;
            barFill.setWidth(Math.max(0, 35.0 * ratio));
            barFill.setFill(ratio > 0.4 ? Color.LIMEGREEN : Color.GOLD);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getLevel() { return level; }
    public int getMaxBlocks() { return maxBlocks; }
    public int getRemainingBlocks() { return maxBlocks - enemiesDestroyed; }
    public int getCooldownDuration() { return cooldownDuration; }
    public int getCooldownTimer() { return cooldownTimer; }
    public boolean isOnCooldown() { return onCooldown; }
    public Node getGraphic() { return graphic; }
}