package jpwp;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Antivirus {

    public enum TowerType {
        AV("antywirus", 75, 2, 10, Color.GOLD),
        IDS("skaner IDS", 90, 3, 12, Color.LIME),
        DEC("dekryptor", 110, 2, 22, Color.MEDIUMORCHID);

        private final String displayName;
        private final int cost;
        private final int baseRange;
        private final int baseDamage;
        private final Color laserColor;

        TowerType(String displayName, int cost, int baseRange, int baseDamage, Color laserColor) {
            this.displayName = displayName;
            this.cost = cost;
            this.baseRange = baseRange;
            this.baseDamage = baseDamage;
            this.laserColor = laserColor;
        }

        public String getDisplayName() { return displayName; }
        public int getCost() { return cost; }
        public int getBaseRange() { return baseRange; }
        public int getBaseDamage() { return baseDamage; }
        public Color getLaserColor() { return laserColor; }
    }

    private int x;
    private int y;
    private int level;
    private int range;
    private int baseDamage;
    private TowerType towerType;
    private int totalInvested;

    private StackPane graphic;
    private Label levelTag;

    public Antivirus(int x, int y, TowerType towerType) {
        this.x = x;
        this.y = y;
        this.towerType = towerType;
        this.level = 1;
        this.range = towerType.getBaseRange();
        this.baseDamage = towerType.getBaseDamage();
        this.totalInvested = towerType.getCost();

        this.graphic = new StackPane();
        this.graphic.setPrefSize(45, 45);
        this.graphic.setMinSize(45, 45);
        this.graphic.setMaxSize(45, 45);

        this.levelTag = new Label("1");
        this.levelTag.setTextFill(towerType == TowerType.DEC ? Color.WHITE : Color.BLACK);
        this.levelTag.setFont(Font.font("System", FontWeight.BOLD, 10));

        if (towerType == TowerType.IDS) {
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
        } else if (towerType == TowerType.DEC) {
            Polygon diamond = new Polygon(
                22.5, 4.0,
                41.0, 22.5,
                22.5, 41.0,
                4.0, 22.5
            );
            diamond.setFill(Color.INDIGO);
            diamond.setStroke(Color.MEDIUMORCHID);
            diamond.setStrokeWidth(2.5);

            Circle core = new Circle(7);
            core.setFill(Color.PLUM);

            this.graphic.getChildren().addAll(diamond, core, levelTag);
        } else {
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
        return level * (int) (towerType.getCost() * 0.8);
    }

    public void upgrade() {
        if (canUpgrade()) {
            totalInvested += getUpgradeCost();
            level++;
            baseDamage += (towerType == TowerType.DEC) ? 8 : 5;
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
    public TowerType getTowerType() { return towerType; }
    public Color getLaserColor() { return towerType.getLaserColor(); }
    public int getX() { return x; }
    public int getY() { return y; }
    public Node getGraphic() { return graphic; }
}