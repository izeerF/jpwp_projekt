package jpwp;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Virus {
    private int x;
    private int y;
    private int hp;
    private EnemyType type;
    private boolean[][] visited = new boolean[12][12];
    private StackPane graphic;
    private Circle body;
    private Label stackLabel;

    public Virus(int startX, int startY, EnemyType type) {
        this.x = startX;
        this.y = startY;
        this.type = type;
        this.hp = type.getMaxHp();

        this.graphic = new StackPane();
        this.graphic.setPrefSize(45, 45);
        this.graphic.setMinSize(45, 45);
        this.graphic.setMaxSize(45, 45);

        this.body = new Circle(16, type.getMainColor());
        this.body.setStroke(type.getStrokeColor());
        this.body.setStrokeWidth(2);

        this.stackLabel = new Label("");
        this.stackLabel.setTextFill(Color.WHITE);
        this.stackLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        this.stackLabel.setVisible(false);

        this.graphic.getChildren().addAll(body, stackLabel);

        if (startX >= 0 && startX < 12 && startY >= 0 && startY < 12) {
            this.visited[startY][startX] = true;
        }
    }

    public void updateStackDisplay(int count, boolean isTopOfStack) {
        if (!isTopOfStack) {
            this.graphic.setVisible(false);
        } else {
            this.graphic.setVisible(true);
            if (count > 1) {
                this.stackLabel.setText("x" + count);
                this.stackLabel.setVisible(true);
            } else {
                this.stackLabel.setVisible(false);
            }
        }
    }

    public void takeAntivirusDamage(int baseAmount) {
        int actualDamage = (int) Math.round(baseAmount * type.getAntivirusDamageMultiplier());
        this.hp -= actualDamage;
        
        double ratio = (double) this.hp / type.getMaxHp();
        if (ratio <= 0.35) {
            this.body.setOpacity(0.5);
        } else if (ratio <= 0.7) {
            this.body.setOpacity(0.75);
        }
    }

    public boolean isFirewallImmune() {
        return type.isFirewallImmune();
    }

    public int getHp() { return hp; }
    public int getReward() { return type.getReward(); }
    public EnemyType getType() { return type; }

    public void moveRight() { x += 1; markVisited(); }
    public void moveLeft() { x -= 1; markVisited(); }
    public void moveUp() { y -= 1; markVisited(); }
    public void moveDown() { y += 1; markVisited(); }

    public void animateMove(int deltaX, int deltaY) {
        graphic.setTranslateX(-deltaX * 45);
        graphic.setTranslateY(-deltaY * 45);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), graphic);
        tt.setToX(0);
        tt.setToY(0);
        tt.play();
    }

    private void markVisited() {
        if (x >= 0 && x < 12 && y >= 0 && y < 12) {
            visited[y][x] = true;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    
    public boolean hasVisited(int vx, int vy) {
        if (vx >= 0 && vx < 12 && vy >= 0 && vy < 12) {
            return visited[vy][vx];
        }
        return false;
    }
    
    public Node getGraphic() { return graphic; }
}