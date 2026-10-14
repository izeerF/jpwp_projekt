package jpwp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Virus {
    private int x;
    private int y;
    private Circle graphic;

    public Virus(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.graphic = new Circle(20, Color.GREEN);
    }

    public void moveRight() {
        this.x += 1;
    }
    public void moveLeft() {
        this.x -= 1;
    }
    public void moveUp() {
        this.y -= 1;
    }
    public void moveDown() {
        this.y += 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Circle getGraphic() {
        return graphic;
    }
}