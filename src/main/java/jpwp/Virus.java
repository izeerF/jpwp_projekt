package jpwp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Virus {
    private int x;
    private int y;
    private int prevX;
    private int prevY;
    private Circle graphic;

    public Virus(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.prevX = startX;
        this.prevY = startY;
        this.graphic = new Circle(20, Color.GREEN);
    }

    public void moveRight() { prevX = x; prevY = y; x += 1; }
    public void moveLeft() { prevX = x; prevY = y; x -= 1; }
    public void moveUp() { prevX = x; prevY = y; y -= 1; }
    public void moveDown() { prevX = x; prevY = y; y += 1; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getPrevX() { return prevX; }
    public int getPrevY() { return prevY; }
    
    public Circle getGraphic() { return graphic; }
}