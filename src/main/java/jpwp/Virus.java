package jpwp;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class Virus {
    private int gridX;
    private int gridY;
    private int targetGridX;
    private int targetGridY;
    private boolean hasTarget;

    private double pixelX;
    private double pixelY;

    private int hp;
    private EnemyType type;
    private boolean[][] visited = new boolean[12][12];
    private StackPane graphic;
    private Circle body;

    public Virus(int startX, int startY, EnemyType type) {
        this.gridX = startX;
        this.gridY = startY;
        this.targetGridX = startX;
        this.targetGridY = startY;
        this.hasTarget = false;

        this.type = type;
        this.hp = type.getMaxHp();

        this.pixelX = (startX + 1) * 45.0 + 22.5;
        this.pixelY = startY * 45.0 + 22.5;

        this.graphic = new StackPane();
        this.graphic.setPrefSize(45, 45);
        this.graphic.setMinSize(45, 45);
        this.graphic.setMaxSize(45, 45);
        this.graphic.setLayoutX(pixelX - 22.5);
        this.graphic.setLayoutY(pixelY - 22.5);

        this.body = new Circle(15, type.getMainColor());
        this.body.setStroke(type.getStrokeColor());
        this.body.setStrokeWidth(2);

        this.graphic.getChildren().add(body);

        if (startX >= 0 && startX < 12 && startY >= 0 && startY < 12) {
            this.visited[startY][startX] = true;
        }
    }

    public void setNextTarget(int nextCol, int nextRow) {
        this.targetGridX = nextCol;
        this.targetGridY = nextRow;
        this.hasTarget = true;
        if (nextCol >= 0 && nextCol < 12 && nextRow >= 0 && nextRow < 12) {
            this.visited[nextRow][nextCol] = true;
        }
    }

    public boolean moveTowardsTarget(double dtSeconds, double speedMultiplier) {
        if (!hasTarget) return true;

        double targetPx = (targetGridX + 1) * 45.0 + 22.5;
        double targetPy = targetGridY * 45.0 + 22.5;

        double dx = targetPx - pixelX;
        double dy = targetPy - pixelY;
        double dist = Math.hypot(dx, dy);
        double step = type.getSpeedPxPerSec() * speedMultiplier * dtSeconds;

        if (dist <= step) {
            pixelX = targetPx;
            pixelY = targetPy;
            gridX = targetGridX;
            gridY = targetGridY;
            hasTarget = false;
            updateGraphicPosition();
            return true;
        } else {
            pixelX += (dx / dist) * step;
            pixelY += (dy / dist) * step;
            updateGraphicPosition();
            return false;
        }
    }

    private void updateGraphicPosition() {
        this.graphic.setLayoutX(pixelX - 22.5);
        this.graphic.setLayoutY(pixelY - 22.5);
    }

    public void takeTowerDamage(int baseAmount, boolean fromIds) {
        double mult = fromIds ? type.getIdsDamageMultiplier() : type.getAntivirusDamageMultiplier();
        int actualDamage = (int) Math.round(baseAmount * mult);
        if (actualDamage < 1) actualDamage = 1;
        this.hp -= actualDamage;

        double ratio = (double) this.hp / type.getMaxHp();
        if (ratio <= 0.35) {
            this.body.setOpacity(0.5);
        } else if (ratio <= 0.7) {
            this.body.setOpacity(0.75);
        }
    }

    public boolean isFirewallImmune() { return type.isFirewallImmune(); }
    public int getHp() { return hp; }
    public int getReward() { return type.getReward(); }
    public EnemyType getType() { return type; }
    public boolean hasTarget() { return hasTarget; }

    public int getX() { return gridX; }
    public int getY() { return gridY; }
    public double getPixelX() { return pixelX; }
    public double getPixelY() { return pixelY; }

    public boolean hasVisited(int vx, int vy) {
        if (vx >= 0 && vx < 12 && vy >= 0 && vy < 12) {
            return visited[vy][vx];
        }
        return false;
    }

    public Node getGraphic() { return graphic; }
}