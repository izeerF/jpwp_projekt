package jpwp;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Antivirus {
    private int x;
    private int y;
    private int level;
    private int range;
    private int baseDamage;
    private boolean isIds;
    private int totalInvested;

    private StackPane graphic;
    private Label levelTag;

    public Antivirus(int x, int y, boolean isIds) {
        this.x = x;
        this.y = y;
        this.isIds = isIds;
        this.level = 1;
        this.totalInvested = isIds ? 90 : 75;

        this.graphic = new StackPane();
        this.graphic.setPrefSize(45, 45);
        this.graphic.setMinSize(45, 45);
        this.graphic.setMaxSize(45, 45);

        this.levelTag = new Label("1");
        this.levelTag.setTextFill(Color.BLACK);
        this.levelTag.setFont(Font.font("System", FontWeight.BOLD, 10));

        if (isIds) {
            this.range = 3;
            this.baseDamage = 12;

            Rectangle base = new Rectangle(34, 34);
            base.setArcWidth(10);
            base.setArcHeight(10);
            base.setFill(Color.DARKSLATEGRAY);
            base.setStroke(Color.LIME);
            base.setStrokeWidth(2.5);
            base.setStrokeType(StrokeType.INSIDE);

            Circle radar = new Circle(9);
            radar.setFill(Color.LIME);

            this.graphic.getChildren().addAll(base, radar, levelTag);
        } else {
            this.range = 2;
            this.baseDamage = 10;

            Circle outerRing = new Circle(18);
            outerRing.setFill(Color.NAVY);
            outerRing.setStroke(Color.CYAN);
            outerRing.setStrokeWidth(3);
            outerRing.setStrokeType(StrokeType.INSIDE);

            Circle innerCore = new Circle(8);
            innerCore.setFill(Color.CYAN);

            this.graphic.getChildren().addAll(outerRing, innerCore, levelTag);
        }
    }

    public boolean canUpgrade() {
        return level < 3;
    }

    public int getUpgradeCost() {
        return isIds ? level * 70 : level * 60;
    }

    public void upgrade() {
        if (canUpgrade()) {
            totalInvested += getUpgradeCost();
            level++;
            baseDamage += 5;
            if (level == 3) {
                range += 1;
            }
            levelTag.setText(String.valueOf(level));
        }
    }

    public int getSellValue() {
        return totalInvested / 2;
    }

public boolean isInRange(double targetPixelX, double targetPixelY) {
        double towerPx = (this.x + 1) * 45.0 + 22.5;
        double towerPy = this.y * 45.0 + 22.5;
        double maxDistPx = (range + 0.5) * 45.0;
        return Math.hypot(towerPx - targetPixelX, towerPy - targetPixelY) <= maxDistPx;
    }

    public int getLevel() { return level; }
    public int getRange() { return range; }
    public int getBaseDamage() { return baseDamage; }
    public boolean isIds() { return isIds; }
    public Color getLaserColor() { return isIds ? Color.LIME : Color.GOLD; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Node getGraphic() { return graphic; }
}