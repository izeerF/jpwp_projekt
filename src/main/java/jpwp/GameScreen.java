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
    private Firewall[][] activeFirewalls = new Firewall[12][12];
    private Timeline gameLoop;

    private int currentSpawnIndex = 0;
    private int[][] intersectionState = new int[12][12];

    public Scene getScene(Stage primaryStage, Scene menuScene) {
        loadMap();

        BorderPane root = new BorderPane();

        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setStyle("-fx-background-color: #e0e0e0;");

        hpLabel = new Label("hp: " + hp);
        moneyLabel = new Label("budzet: " + money + " pgCoinow");
        Button buyFirewallBtn = new Button("kup firewall (koszt: 50)");
        Button spawnVirusBtn = new Button("virus test");
        Button backButton = new Button("wroc do menu");

        GridPane gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);

        buyFirewallBtn.setOnAction(e -> {
            if (isBuyingMode) {
                isBuyingMode = false;
                buyFirewallBtn.setText("kup firewall (koszt: 50)");
                toggleHighlight(false);
            } else if (money >= 50) {
                isBuyingMode = true;
                buyFirewallBtn.setText("anuluj");
                toggleHighlight(true);
            }
        });

        spawnVirusBtn.setOnAction(e -> {
            List<Integer> spawns = new ArrayList<>();
            for (int r = 0; r < 12; r++) {
                if (mapGrid[r][0] == 1) spawns.add(r);
            }
            if (!spawns.isEmpty()) {
                int spawnY = spawns.get(currentSpawnIndex % spawns.size());
                currentSpawnIndex++;
                
                Virus virus = new Virus(0, spawnY);
                activeViruses.add(virus);
                gameBoard.add(virus.getGraphic(), virus.getX() + 1, virus.getY());
            }
        });

        backButton.setOnAction(e -> {
            if (gameLoop != null) gameLoop.stop();
            primaryStage.setScene(menuScene);
        });

        rightPanel.getChildren().addAll(hpLabel, moneyLabel, buyFirewallBtn, spawnVirusBtn, backButton);
        root.setRight(rightPanel);

        for (int row = 0; row < 12; row++) {
            if (mapGrid[row][0] == 1) {
                Rectangle internetBlock = new Rectangle(45, 45, Color.BLUE);
                internetBlock.setStrokeType(StrokeType.INSIDE);
                internetBlock.setStroke(Color.BLACK);
                gameBoard.add(internetBlock, 0, row);
            }
            
            if (mapGrid[row][11] == 1) {
                Rectangle serverBlock = new Rectangle(45, 45, Color.PURPLE);
                serverBlock.setStrokeType(StrokeType.INSIDE);
                serverBlock.setStroke(Color.BLACK);
                gameBoard.add(serverBlock, 13, row);
            }

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
                        moneyLabel.setText("budzet: " + money + " pgCoinow");
                        isBuyingMode = false;
                        buyFirewallBtn.setText("kup firewall (koszt: 50)");
                        
                        mapGrid[finalRow][finalCol] = 3;
                        toggleHighlight(false);
                        
                        Firewall firewall = new Firewall(finalCol, finalRow);
                        activeFirewalls[finalRow][finalCol] = firewall;
                        gameBoard.add(firewall.getGraphic(), finalCol + 1, finalRow);
                    }
                });
                
                gameBoard.add(tile, col + 1, row);
            }
        }
        
        root.setCenter(gameBoard);

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            
            for (int r = 0; r < 12; r++) {
                for (int c = 0; c < 12; c++) {
                    if (activeFirewalls[r][c] != null) {
                        activeFirewalls[r][c].updateCooldown();
                    }
                }
            }
            
            List<Virus> toRemove = new ArrayList<>();
            for (Virus virus : activeViruses) {
                int vx = virus.getX();
                int vy = virus.getY();

                if (vx == 11) {
                    gameBoard.getChildren().remove(virus.getGraphic());
                    toRemove.add(virus);
                    System.out.println("-10hp");
                    
                    hp -= 10;
                    hpLabel.setText("hp: " + hp);
                    
                    if (hp <= 0) {
                        System.out.println("game over");
                        gameLoop.stop();
                        primaryStage.setScene(menuScene);
                    }
                } else {
                    boolean canUp = vy > 0 && (mapGrid[vy - 1][vx] == 1 || mapGrid[vy - 1][vx] == 3) && !virus.hasVisited(vx, vy - 1);
                    boolean canDown = vy < 11 && (mapGrid[vy + 1][vx] == 1 || mapGrid[vy + 1][vx] == 3) && !virus.hasVisited(vx, vy + 1);
                    boolean canRight = vx < 11 && (mapGrid[vy][vx + 1] == 1 || mapGrid[vy][vx + 1] == 3) && !virus.hasVisited(vx + 1, vy);
                    boolean canLeft = vx > 0 && (mapGrid[vy][vx - 1] == 1 || mapGrid[vy][vx - 1] == 3) && !virus.hasVisited(vx - 1, vy);

                    int nextX = vx;
                    int nextY = vy;

                    if (canRight) {
                        nextX = vx + 1;
                    } else if (canUp && canDown) {
                        if (intersectionState[vy][vx] % 2 == 0) {
                            nextY = vy - 1;
                        } else {
                            nextY = vy + 1;
                        }
                        intersectionState[vy][vx]++;
                    } else if (canUp) {
                        nextY = vy - 1;
                    } else if (canDown) {
                        nextY = vy + 1;
                    } else if (canLeft) {
                        nextX = vx - 1;
                    }

                    if (nextX != vx || nextY != vy) {
                        boolean destroyed = false;
                        
                        if (mapGrid[nextY][nextX] == 3) {
                            Firewall fw = activeFirewalls[nextY][nextX];
                            if (fw != null && !fw.isOnCooldown()) {
                                fw.destroyEnemy();
                                gameBoard.getChildren().remove(virus.getGraphic());
                                toRemove.add(virus);
                                destroyed = true;
                            }
                        }
                        
                        if (!destroyed) {
                            if (nextX > vx) virus.moveRight();
                            else if (nextX < vx) virus.moveLeft();
                            else if (nextY > vy) virus.moveDown();
                            else if (nextY < vy) virus.moveUp();
                            
                            GridPane.setColumnIndex(virus.getGraphic(), virus.getX() + 1);
                            GridPane.setRowIndex(virus.getGraphic(), virus.getY());
                        }
                    }
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