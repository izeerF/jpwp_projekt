package jpwp;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Antivirus {
    private int x;
    private int y;
    private int range;
    private int baseDamage;
    private boolean isIds;
    private StackPane graphic;

    public Antivirus(int x, int y, boolean isIds) {
        this.x = x;
        this.y = y;
        this.isIds = isIds;

        this.graphic = new StackPane();
        this.graphic.setPrefSize(45, 45);
        this.graphic.setMinSize(45, 45);
        this.graphic.setMaxSize(45, 45);

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

            Circle radar = new Circle(8);
            radar.setFill(Color.LIME);

            this.graphic.getChildren().addAll(base, radar);
        } else {
            this.range = 2;
            this.baseDamage = 10;

            Circle outerRing = new Circle(18);
            outerRing.setFill(Color.NAVY);
            outerRing.setStroke(Color.CYAN);
            outerRing.setStrokeWidth(3);
            outerRing.setStrokeType(StrokeType.INSIDE);

            Circle innerCore = new Circle(7);
            innerCore.setFill(Color.CYAN);

            this.graphic.getChildren().addAll(outerRing, innerCore);
        }
    }

    public boolean isInRange(int targetX, int targetY) {
        int dx = Math.abs(this.x - targetX);
        int dy = Math.abs(this.y - targetY);
        return dx <= range && dy <= range;
    }

    public int getBaseDamage() { return baseDamage; }
    public boolean isIds() { return isIds; }
    public Color getLaserColor() { return isIds ? Color.LIME : Color.GOLD; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Node getGraphic() { return graphic; }
}