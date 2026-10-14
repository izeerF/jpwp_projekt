package jpwp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameScreen {

    private int hp = 100;
    private int money = 100;
    private boolean isBuyingMode = false;
    private Label hpLabel;
    private Label moneyLabel;
    private List<Virus> activeViruses = new ArrayList<>();
    
    private int[][] mapGrid = new int[12][12];
    private Rectangle[][] tilesGrid = new Rectangle[12][12];
    private Timeline gameLoop;

    public Scene getScene(Stage primaryStage, Scene menuScene) {
        loadMap();

        BorderPane root = new BorderPane();

        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setStyle("-fx-background-color: #e0e0e0;");

        hpLabel = new Label("HP: " + hp);
        moneyLabel = new Label("Budzet: " + money + " pgCoinow");
        Button buyFirewallBtn = new Button("Kup Firewall (Koszt: 50)");
        Button spawnVirusBtn = new Button("Virus Test");
        Button backButton = new Button("Wroc do menu");

        GridPane gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);

        buyFirewallBtn.setOnAction(e -> {
            if (money >= 50 && !isBuyingMode) {
                isBuyingMode = true;
                toggleHighlight(true);
            }
        });

        spawnVirusBtn.setOnAction(e -> {
            Virus virus = new Virus(0, 5);
            activeViruses.add(virus);
            gameBoard.add(virus.getGraphic(), virus.getX(), virus.getY());
        });

        backButton.setOnAction(e -> {
            if (gameLoop != null) gameLoop.stop();
            primaryStage.setScene(menuScene);
        });

        rightPanel.getChildren().addAll(hpLabel, moneyLabel, buyFirewallBtn, spawnVirusBtn, backButton);
        root.setRight(rightPanel);

        for (int row = 0; row < 12; row++) {
            for (int col = 0; col < 12; col++) {
                Rectangle tile = new Rectangle(45, 45);
                tile.setStrokeType(StrokeType.INSIDE);
                tilesGrid[row][col] = tile;
                
                if (mapGrid[row][col] == 1) {
                    tile.setFill(Color.DARKGRAY);
                } else if (mapGrid[row][col] == 2) {
                    tile.setFill(Color.LIGHTBLUE);
                } else {
                    tile.setFill(Color.WHITE);
                }
                
                tile.setStroke(Color.TRANSPARENT);
                tile.setStrokeWidth(1);
                
                final int finalRow = row;
                final int finalCol = col;
                
                tile.setOnMouseClicked(e -> {
                    if (isBuyingMode && mapGrid[finalRow][finalCol] == 1) {
                        money -= 50;
                        moneyLabel.setText("Budzet: " + money + " pgCoinow");
                        isBuyingMode = false;
                        
                        mapGrid[finalRow][finalCol] = 3;
                        
                        toggleHighlight(false);
                        
                        Firewall firewall = new Firewall(finalCol, finalRow);
                        gameBoard.add(firewall.getGraphic(), finalCol, finalRow);
                    }
                });
                
                gameBoard.add(tile, col, row);
            }
        }
        
        root.setCenter(gameBoard);

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            List<Virus> toRemove = new ArrayList<>();
            for (Virus virus : activeViruses) {
                int vx = virus.getX();
                int vy = virus.getY();

                if (vx == 11) {
                    gameBoard.getChildren().remove(virus.getGraphic());
                    toRemove.add(virus);
                    System.out.println("-10HP");
                    
                    hp -= 10;
                    hpLabel.setText("HP: " + hp);
                    
                    if (hp <= 0) {
                        System.out.println("Game Over");
                        gameLoop.stop();
                        primaryStage.setScene(menuScene);
                    }
                } else {
                    if (vy > 0 && (mapGrid[vy - 1][vx] == 1 || mapGrid[vy - 1][vx] == 3) && virus.getPrevY() != vy - 1) {
                        virus.moveUp();
                    } else if (vy < 11 && (mapGrid[vy + 1][vx] == 1 || mapGrid[vy + 1][vx] == 3) && virus.getPrevY() != vy + 1) {
                        virus.moveDown();
                    } else if (vx < 11 && (mapGrid[vy][vx + 1] == 1 || mapGrid[vy][vx + 1] == 3) && virus.getPrevX() != vx + 1) {
                        virus.moveRight();
                    } else if (vx > 0 && (mapGrid[vy][vx - 1] == 1 || mapGrid[vy][vx - 1] == 3) && virus.getPrevX() != vx - 1) {
                        virus.moveLeft();
                    }
                    
                    GridPane.setColumnIndex(virus.getGraphic(), virus.getX());
                    GridPane.setRowIndex(virus.getGraphic(), virus.getY());
                }
            }
            activeViruses.removeAll(toRemove);
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        return new Scene(root, 800, 600);
    }

    private void toggleHighlight(boolean show) {
        for (int r = 0; r < 12; r++) {
            for (int c = 0; c < 12; c++) {
                if (show) {
                    if (mapGrid[r][c] == 1) {
                        tilesGrid[r][c].setStroke(Color.LIMEGREEN);
                        tilesGrid[r][c].setStrokeWidth(3);
                    }
                } else {
                    tilesGrid[r][c].setStroke(Color.TRANSPARENT);
                    tilesGrid[r][c].setStrokeWidth(1);
                }
            }
        }
    }

    private void loadMap() {
        try {
            InputStream is = getClass().getResourceAsStream("/map.txt");
            Scanner scanner = new Scanner(is);
            for (int row = 0; row < 12; row++) {
                for (int col = 0; col < 12; col++) {
                    mapGrid[row][col] = scanner.nextInt();
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("nie udalo sie zaladowac pliku mapy");
        }
    }
}