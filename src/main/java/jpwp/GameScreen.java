package jpwp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameScreen {

    public Scene getScene(Stage primaryStage, Scene menuScene) {
        BorderPane root = new BorderPane();

        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setStyle("-fx-background-color: #e0e0e0;");

        Label moneyLabel = new Label("Budzet: 100 pgCoinow");
        Button buyFirewallBtn = new Button("Kup Firewall");
        Button backButton = new Button("Wroc do menu");

        backButton.setOnAction(e -> primaryStage.setScene(menuScene));

        rightPanel.getChildren().addAll(moneyLabel, buyFirewallBtn, backButton);
        root.setRight(rightPanel);

        GridPane gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);
        
        // 12x12 grid
        for (int row = 0; row < 12; row++) {
            for (int col = 0; col < 12; col++) {
                Rectangle tile = new Rectangle(45, 45);
                tile.setFill(Color.WHITE);
                tile.setStroke(Color.LIGHTGRAY);
                
                //square onclick
                tile.setOnMouseClicked(e -> {
                    System.out.println("square clicked");
                    tile.setFill(Color.BLUE);
                });
                
                gameBoard.add(tile, col, row);
            }
        }
        
        root.setCenter(gameBoard);

        return new Scene(root, 800, 600);
    }
}