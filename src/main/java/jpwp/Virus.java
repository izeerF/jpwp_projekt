package jpwp;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Virus {
    private int x;
    private int y;
    private int hp;
    private boolean[][] visited = new boolean[12][12];
    private StackPane graphic;
    private Circle body;

    public Virus(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.hp = 30;

        this.graphic = new StackPane();
        this.graphic.setPrefSize(45, 45);
        this.graphic.setMinSize(45, 45);
        this.graphic.setMaxSize(45, 45);

        this.body = new Circle(16, Color.GREEN);
        this.body.setStroke(Color.DARKGREEN);
        this.body.setStrokeWidth(2);
        this.graphic.getChildren().add(body);

        if (startX >= 0 && startX < 12 && startY >= 0 && startY < 12) {
            this.visited[startY][startX] = true;
        }
    }

    public void takeDamage(int amount) {
        this.hp -= amount;
        if (this.hp == 20) {
            this.body.setFill(Color.LIMEGREEN);
        } else if (this.hp <= 10) {
            this.body.setFill(Color.YELLOWGREEN);
        }
    }

    public int getHp() {
        return hp;
    }

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