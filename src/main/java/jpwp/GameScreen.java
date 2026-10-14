package jpwp;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

public class GameScreen {

    private int money = 100;
    private boolean isBuyingMode = false;
    private Label moneyLabel;
    private List<Virus> activeViruses = new ArrayList<>();

    public Scene getScene(Stage primaryStage, Scene menuScene) {
        BorderPane root = new BorderPane();

        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setStyle("-fx-background-color: #e0e0e0;");

        moneyLabel = new Label("Budzet: " + money + " pgCoinow");
        Button buyFirewallBtn = new Button("Kup Firewall (Koszt: 50)");
        Button spawnVirusBtn = new Button("Wypusc Wirusa");
        Button backButton = new Button("Wroc do menu");

        GridPane gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);

        buyFirewallBtn.setOnAction(e -> {
            if (money >= 50) {
                isBuyingMode = true;
                System.out.println("Kliknij na wolne pole, aby postawic zapore.");
            } else {
                System.out.println("Brak srodkow!");
            }
        });

        spawnVirusBtn.setOnAction(e -> {
            Virus virus = new Virus(0, 5);
            activeViruses.add(virus);
            gameBoard.add(virus.getGraphic(), virus.getX(), virus.getY());
        });

        backButton.setOnAction(e -> primaryStage.setScene(menuScene));

        rightPanel.getChildren().addAll(moneyLabel, buyFirewallBtn, spawnVirusBtn, backButton);
        root.setRight(rightPanel);

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
                        
                        System.out.println("Postawiono Firewall!");
                    }
                });
                
                gameBoard.add(tile, col, row);
            }
        }
        
        root.setCenter(gameBoard);

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            List<Virus> toRemove = new ArrayList<>();
            for (Virus virus : activeViruses) {
                if (virus.getX() < 11) {
                    virus.moveRight();
                    GridPane.setColumnIndex(virus.getGraphic(), virus.getX());
                } else {
                    gameBoard.getChildren().remove(virus.getGraphic());
                    toRemove.add(virus);
                    System.out.println("-10HP");
                }
            }
            activeViruses.removeAll(toRemove);
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        return new Scene(root, 800, 600);
    }
}