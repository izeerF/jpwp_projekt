package jpwp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Firewall {
    private int x;
    private int y;
    private Rectangle graphic;

    public Firewall(int x, int y) {
        this.x = x;
        this.y = y;
        
        this.graphic = new Rectangle(44, 44);
        this.graphic.setFill(Color.RED);
        this.graphic.setStroke(Color.BLACK);
    }

    public Rectangle getGraphic() {
        return graphic;
    }
}