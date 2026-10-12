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

    private int money = 100;
    private boolean isBuyingMode = false;
    private Label moneyLabel;

    public Scene getScene(Stage primaryStage, Scene menuScene) {
        BorderPane root = new BorderPane();

        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setStyle("-fx-background-color: #e0e0e0;");

        moneyLabel = new Label("Budzet: " + money + " pgCoinow");
        Button buyFirewallBtn = new Button("Kup Firewall (Koszt: 50)");
        Button backButton = new Button("Wroc do menu");

        buyFirewallBtn.setOnAction(e -> {
            if (money >= 50) {
                isBuyingMode = true;
                System.out.println("kliknij aby postawic firewall");
            } else {
                System.out.println("masz niewystarczajaco pgCoinow");
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(menuScene));

        rightPanel.getChildren().addAll(moneyLabel, buyFirewallBtn, backButton);
        root.setRight(rightPanel);

        GridPane gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);
        
        for (int row = 0; row < 12; row++) {
            for (int col = 0; col < 12; col++) {
                Rectangle tile = new Rectangle(45, 45);
                tile.setFill(Color.WHITE);
                tile.setStroke(Color.LIGHTGRAY);
                
                final int finalRow = row;
                final int finalCol = col;
                
                tile.setOnMouseClicked(e -> {
                    if (isBuyingMode && tile.getFill() == Color.WHITE) {
                        money -= 50;
                        moneyLabel.setText("Budzet: " + money + " pgCoinow");
                        isBuyingMode = false;
                        
                        Firewall firewall = new Firewall(finalCol, finalRow);
                        gameBoard.add(firewall.getGraphic(), finalCol, finalRow);
                        
                        System.out.println("postawiono firewall");
                    }
                });
                
                gameBoard.add(tile, col, row);
            }
        }
        
        root.setCenter(gameBoard);

        return new Scene(root, 800, 600);
    }
}