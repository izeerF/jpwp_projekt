package jpwp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Firewall {
    private int x;
    private int y;
    private int enemiesDestroyed;
    private int cooldownTimer;
    private boolean onCooldown;
    private Rectangle graphic;

    public Firewall(int x, int y) {
        this.x = x;
        this.y = y;
        this.enemiesDestroyed = 0;
        this.cooldownTimer = 0;
        this.onCooldown = false;
        this.graphic = new Rectangle(44, 44);
        this.graphic.setFill(Color.RED);
        this.graphic.setStroke(Color.BLACK);
    }

    public void destroyEnemy() {
        if (!onCooldown) {
            enemiesDestroyed++;
            if (enemiesDestroyed >= 5) {
                onCooldown = true;
                cooldownTimer = 5;
                graphic.setFill(Color.GRAY);
            }
        }
    }

    public void updateCooldown() {
        if (onCooldown) {
            cooldownTimer--;
            if (cooldownTimer <= 0) {
                onCooldown = false;
                enemiesDestroyed = 0;
                graphic.setFill(Color.RED);
            }
        }
    }

    public boolean isOnCooldown() {
        return onCooldown;
    }

    public Rectangle getGraphic() {
        return graphic;
    }
}