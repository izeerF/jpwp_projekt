package jpwp;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Antivirus {
    private int x;
    private int y;
    private int range;
    private int damage;
    private StackPane graphic;

    public Antivirus(int x, int y) {
        this.x = x;
        this.y = y;
        this.range = 2;
        this.damage = 10;

        this.graphic = new StackPane();
        this.graphic.setPrefSize(45, 45);
        this.graphic.setMinSize(45, 45);
        this.graphic.setMaxSize(45, 45);

        Circle outerRing = new Circle(18);
        outerRing.setFill(Color.NAVY);
        outerRing.setStroke(Color.CYAN);
        outerRing.setStrokeWidth(3);
        outerRing.setStrokeType(StrokeType.INSIDE);

        Circle innerCore = new Circle(7);
        innerCore.setFill(Color.CYAN);

        this.graphic.getChildren().addAll(outerRing, innerCore);
    }

    public boolean isInRange(int targetX, int targetY) {
        int dx = Math.abs(this.x - targetX);
        int dy = Math.abs(this.y - targetY);
        return dx <= range && dy <= range;
    }

    public int getDamage() {
        return damage;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Node getGraphic() { return graphic; }
}