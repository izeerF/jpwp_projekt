package jpwp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameScreen {

    private int hp = 100;
    private int money = 150;
    private int waveNumber = 0;
    private boolean isWaveActive = false;
    private int spawnedInCurrentWave = 0;

    private List<WaveData> levelWaves = new ArrayList<>();
    private WaveData currentWaveData = null;

    private int buyingType = 0; // 0 - nic, 1 - firewall, 2 - antivirus
    private Label hpLabel;
    private Label moneyLabel;
    private Label waveLabel;
    private Button startWaveBtn;

    private List<Virus> activeViruses = new ArrayList<>();
    private List<Antivirus> activeTowers = new ArrayList<>();
    
    private int[][] mapGrid = new int[12][12];
    private Rectangle[][] tilesGrid = new Rectangle[12][12];
    private Firewall[][] activeFirewalls = new Firewall[12][12];
    private Timeline gameLoop;

    private int currentSpawnIndex = 0;
    private int[][] intersectionState = new int[12][12];

    public Scene getScene(Stage primaryStage, Scene menuScene) {
        loadMap("/map_level1.txt");
        loadWaves("/waves_level1.txt");

        BorderPane root = new BorderPane();

        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setStyle("-fx-background-color: #e0e0e0;");
        
        rightPanel.setMinWidth(250);
        rightPanel.setPrefWidth(250);

        waveLabel = new Label("fala: 0 / " + levelWaves.size());
        hpLabel = new Label("hp: " + hp);
        moneyLabel = new Label("budzet: " + money + " pgCoinow");
        
        Button buyFirewallBtn = new Button("kup firewall (koszt: 50)");
        buyFirewallBtn.setMinWidth(190);
        
        Button buyTowerBtn = new Button("kup antywirus (koszt: 75)");
        buyTowerBtn.setMinWidth(190);
        
        startWaveBtn = new Button("rozpocznij fale 1");
        startWaveBtn.setMinWidth(190);
        if (levelWaves.isEmpty()) {
            startWaveBtn.setText("brak fal w pliku");
            startWaveBtn.setDisable(true);
        }
        
        Button backButton = new Button("wroc do menu");
        backButton.setMinWidth(190);

        GridPane gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);

        Pane effectsLayer = new Pane();
        effectsLayer.setMouseTransparent(true);
        effectsLayer.setPrefSize(14 * 45, 12 * 45);
        effectsLayer.setMaxSize(14 * 45, 12 * 45);
        effectsLayer.setMinSize(14 * 45, 12 * 45);

        StackPane boardContainer = new StackPane(gameBoard, effectsLayer);
        boardContainer.setPadding(new Insets(0, 40, 0, 40));

        buyFirewallBtn.setOnAction(e -> {
            if (buyingType == 1) {
                buyingType = 0;
                buyFirewallBtn.setText("kup firewall (koszt: 50)");
                toggleHighlight(0);
            } else if (money >= 50) {
                buyingType = 1;
                buyFirewallBtn.setText("anuluj");
                buyTowerBtn.setText("kup antywirus (koszt: 75)");
                toggleHighlight(1);
            }
        });

        buyTowerBtn.setOnAction(e -> {
            if (buyingType == 2) {
                buyingType = 0;
                buyTowerBtn.setText("kup antywirus (koszt: 75)");
                toggleHighlight(0);
            } else if (money >= 75) {
                buyingType = 2;
                buyTowerBtn.setText("anuluj");
                buyFirewallBtn.setText("kup firewall (koszt: 50)");
                toggleHighlight(2);
            }
        });

        startWaveBtn.setOnAction(e -> {
            if (!isWaveActive && waveNumber < levelWaves.size()) {
                currentWaveData = levelWaves.get(waveNumber);
                waveNumber++;
                isWaveActive = true;
                spawnedInCurrentWave = 0;
                
                waveLabel.setText("fala: " + waveNumber + " / " + levelWaves.size());
                startWaveBtn.setText("trwa fala " + waveNumber + "...");
                startWaveBtn.setDisable(true);
            }
        });

        backButton.setOnAction(e -> {
            if (gameLoop != null) gameLoop.stop();
            primaryStage.setScene(menuScene);
        });

        rightPanel.getChildren().addAll(waveLabel, hpLabel, moneyLabel, buyFirewallBtn, buyTowerBtn, startWaveBtn, backButton);
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
                    if (buyingType == 1 && mapGrid[finalRow][finalCol] == 1) {
                        money -= 50;
                        moneyLabel.setText("budzet: " + money + " pgCoinow");
                        buyingType = 0;
                        buyFirewallBtn.setText("kup firewall (koszt: 50)");
                        
                        toggleHighlight(0);
                        mapGrid[finalRow][finalCol] = 3;
                        
                        Firewall firewall = new Firewall(finalCol, finalRow);
                        activeFirewalls[finalRow][finalCol] = firewall;
                        gameBoard.add(firewall.getGraphic(), finalCol + 1, finalRow);
                    } else if (buyingType == 2 && mapGrid[finalRow][finalCol] == 2) {
                        money -= 75;
                        moneyLabel.setText("budzet: " + money + " pgCoinow");
                        buyingType = 0;
                        buyTowerBtn.setText("kup antywirus (koszt: 75)");
                        
                        toggleHighlight(0);
                        mapGrid[finalRow][finalCol] = 4;
                        
                        Antivirus tower = new Antivirus(finalCol, finalRow);
                        activeTowers.add(tower);
                        gameBoard.add(tower.getGraphic(), finalCol + 1, finalRow);
                    }
                });
                
                gameBoard.add(tile, col + 1, row);
            }
        }
        
        root.setCenter(boardContainer);

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            
            for (int r = 0; r < 12; r++) {
                for (int c = 0; c < 12; c++) {
                    if (activeFirewalls[r][c] != null) {
                        activeFirewalls[r][c].updateCooldown();
                    }
                }
            }
            
            List<Virus> toRemove = new ArrayList<>();
            
            for (Antivirus tower : activeTowers) {
                for (Virus virus : activeViruses) {
                    if (!toRemove.contains(virus) && tower.isInRange(virus.getX(), virus.getY())) {
                        drawLaser(effectsLayer, tower.getX(), tower.getY(), virus.getX(), virus.getY());
                        virus.takeAntivirusDamage(tower.getDamage());
                        if (virus.getHp() <= 0) {
                            gameBoard.getChildren().remove(virus.getGraphic());
                            toRemove.add(virus);
                            money += virus.getReward();
                            moneyLabel.setText("budzet: " + money + " pgCoinow");
                        }
                        break;
                    }
                }
            }

            for (Virus virus : activeViruses) {
                if (toRemove.contains(virus)) continue;
                
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
                        
                        if (mapGrid[nextY][nextX] == 3 && !virus.isFirewallImmune()) {
                            Firewall fw = activeFirewalls[nextY][nextX];
                            if (fw != null && !fw.isOnCooldown()) {
                                fw.destroyEnemy();
                                gameBoard.getChildren().remove(virus.getGraphic());
                                toRemove.add(virus);
                                destroyed = true;
                            }
                        }
                        
                        if (!destroyed) {
                            int dx = nextX - vx;
                            int dy = nextY - vy;

                            if (dx > 0) virus.moveRight();
                            else if (dx < 0) virus.moveLeft();
                            else if (dy > 0) virus.moveDown();
                            else if (dy < 0) virus.moveUp();
                            
                            GridPane.setColumnIndex(virus.getGraphic(), virus.getX() + 1);
                            GridPane.setRowIndex(virus.getGraphic(), virus.getY());
                            virus.animateMove(dx, dy);
                        }
                    }
                }
            }
            activeViruses.removeAll(toRemove);

            if (isWaveActive && currentWaveData != null) {
                List<EnemyType> sequence = currentWaveData.getEnemySequence();
                if (spawnedInCurrentWave < sequence.size()) {
                    List<Integer> spawns = new ArrayList<>();
                    for (int r = 0; r < 12; r++) {
                        if (mapGrid[r][0] == 1) spawns.add(r);
                    }
                    if (!spawns.isEmpty()) {
                        int spawnY = spawns.get(currentSpawnIndex % spawns.size());
                        currentSpawnIndex++;

                        EnemyType type = sequence.get(spawnedInCurrentWave);
                        spawnedInCurrentWave++;

                        Virus virus = new Virus(0, spawnY, type);
                        activeViruses.add(virus);
                        gameBoard.add(virus.getGraphic(), virus.getX() + 1, virus.getY());
                    }
                } else if (activeViruses.isEmpty()) {
                    isWaveActive = false;
                    
                    if (waveNumber < levelWaves.size()) {
                        startWaveBtn.setText("rozpocznij fale " + (waveNumber + 1));
                        startWaveBtn.setDisable(false);
                    } else {
                        startWaveBtn.setText("poziom ukonczony");
                        startWaveBtn.setDisable(true);
                    }
                }
            }

            updateVirusStacks();
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        return new Scene(root, 1000, 700);
    }

    private void updateVirusStacks() {
        int[][] counts = new int[12][12];
        for (Virus v : activeViruses) {
            counts[v.getY()][v.getX()]++;
        }

        boolean[][] shown = new boolean[12][12];
        for (Virus v : activeViruses) {
            int vx = v.getX();
            int vy = v.getY();
            int count = counts[vy][vx];

            if (!shown[vy][vx]) {
                v.updateStackDisplay(count, true);
                shown[vy][vx] = true;
            } else {
                v.updateStackDisplay(count, false);
            }
        }
    }

    private void drawLaser(Pane layer, int fromCol, int fromRow, int toCol, int toRow) {
        double startX = (fromCol + 1) * 45.0 + 22.5;
        double startY = fromRow * 45.0 + 22.5;
        double endX = (toCol + 1) * 45.0 + 22.5;
        double endY = toRow * 45.0 + 22.5;

        Line laser = new Line(startX, startY, endX, endY);
        laser.setStroke(Color.GOLD);
        laser.setStrokeWidth(3);
        layer.getChildren().add(laser);

        PauseTransition pause = new PauseTransition(Duration.millis(200));
        pause.setOnFinished(e -> layer.getChildren().remove(laser));
        pause.play();
    }

    private void toggleHighlight(int mode) {
        for (int r = 0; r < 12; r++) {
            for (int c = 0; c < 12; c++) {
                if (mapGrid[r][c] == 1) {
                    tilesGrid[r][c].setFill(mode == 1 ? Color.LIMEGREEN : Color.DARKGRAY);
                } else if (mapGrid[r][c] == 2) {
                    tilesGrid[r][c].setFill(mode == 2 ? Color.LIMEGREEN : Color.LIGHTBLUE);
                }
            }
        }
    }

    private void loadMap(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
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

    private void loadWaves(String path) {
        levelWaves.clear();
        try {
            InputStream is = getClass().getResourceAsStream(path);
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] tokens = line.split("\\s+");
                List<EnemyType> seq = new ArrayList<>();
                for (String token : tokens) {
                    seq.add(EnemyType.fromCode(token));
                }
                if (!seq.isEmpty()) {
                    levelWaves.add(new WaveData(seq));
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("nie udalo sie zaladowac pliku fal");
        }
    }
}