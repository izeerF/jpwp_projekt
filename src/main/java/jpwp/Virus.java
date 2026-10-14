package jpwp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Virus {
    private int x;
    private int y;
    private boolean[][] visited = new boolean[12][12];
    private Circle graphic;

    public Virus(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.graphic = new Circle(20, Color.GREEN);
        if (startX >= 0 && startX < 12 && startY >= 0 && startY < 12) {
            this.visited[startY][startX] = true;
        }
    }

    public void moveRight() { x += 1; markVisited(); }
    public void moveLeft() { x -= 1; markVisited(); }
    public void moveUp() { y -= 1; markVisited(); }
    public void moveDown() { y += 1; markVisited(); }

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
    
    public Circle getGraphic() { return graphic; }
}