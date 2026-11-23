package jpwp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameScreen {

    private int currentLevel = 1;
    private final int maxLevels = 3;
    private boolean levelCompleted = false;

    private int hp = 100;
    private int money = 150;
    private int enemiesNeutralized = 0;
    private int waveNumber = 0;
    private boolean isWaveActive = false;
    private int spawnedInCurrentWave = 0;
    private double gameSpeed = 1.0;

    private Set<String> discoveredItems = new HashSet<>();
    private Queue<String> pendingDiscoveries = new LinkedList<>();
    private int pendingLevelIntro = 0;
    private boolean isPopupOpen = false;

    private List<WaveData> levelWaves = new ArrayList<>();
    private WaveData currentWaveData = null;

    private int buyingType = 0; // 0 - nic, 1 - firewall, 2 - antivirus, 3 - ids
    private Label levelTitleLabel;
    private Label levelLabel;
    private Label hpLabel;
    private Label moneyLabel;
    private Label waveLabel;
    private Button startWaveBtn;
    private Button speedBtn;

    private VBox inspectorBox;
    private Label inspectorTitleLabel;
    private Label inspectorStatsLabel;
    private Button upgradeBtn;
    private Button sellBtn;
    private Firewall selectedFirewall = null;
    private Antivirus selectedTower = null;

    private List<Virus> activeViruses = new ArrayList<>();
    private List<Antivirus> activeTowers = new ArrayList<>();
    
    private int[][] mapGrid = new int[12][12];
    private Rectangle[][] tilesGrid = new Rectangle[12][12];
    private Firewall[][] activeFirewalls = new Firewall[12][12];
    private Timeline gameLoop;

    private int currentSpawnIndex = 0;
    private int[][] intersectionState = new int[12][12];

    public Scene getScene(Stage primaryStage, Scene menuScene) {
        return getScene(primaryStage, menuScene, 1);
    }

    public Scene getScene(Stage primaryStage, Scene menuScene, int startLevel) {
        currentLevel = startLevel;
        hp = 100;
        money = 15000 + (startLevel - 1) * 100;
        enemiesNeutralized = 0;
        gameSpeed = 1.0;
        discoveredItems.clear();
        pendingDiscoveries.clear();
        pendingLevelIntro = 0;
        isPopupOpen = false;

        StackPane rootStack = new StackPane();
        BorderPane root = new BorderPane();
        rootStack.getChildren().add(root);

        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(16));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setStyle("-fx-background-color: #e0e0e0;");
        
        rightPanel.setMinWidth(250);
        rightPanel.setPrefWidth(250);

        levelTitleLabel = new Label("sieć domowa");
        levelTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        levelTitleLabel.setTextFill(Color.DARKSLATEGRAY);

        levelLabel = new Label("poziom: " + currentLevel);
        waveLabel = new Label("fala: 0 / 0");
        hpLabel = new Label("hp: " + hp);
        moneyLabel = new Label("budzet: " + money + " pgCoinow");
        
        Button buyFirewallBtn = new Button("kup firewall (koszt: 50)");
        buyFirewallBtn.setMinWidth(195);
        
        Button buyTowerBtn = new Button("kup antywirus (koszt: 75)");
        buyTowerBtn.setMinWidth(195);

        Button buyIdsBtn = new Button("kup skaner IDS (koszt: 90)");
        buyIdsBtn.setMinWidth(195);

        inspectorBox = new VBox(6);
        inspectorBox.setPadding(new Insets(10));
        inspectorBox.setAlignment(Pos.CENTER);
        inspectorBox.setMaxWidth(195);
        inspectorBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; -fx-border-color: #adb5bd; -fx-border-radius: 6;");
        inspectorBox.setVisible(false);
        inspectorBox.setManaged(false);

        inspectorTitleLabel = new Label("");
        inspectorTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        inspectorStatsLabel = new Label("");
        inspectorStatsLabel.setFont(Font.font("System", 11));
        inspectorStatsLabel.setWrapText(true);

        upgradeBtn = new Button("ulepsz");
        upgradeBtn.setMinWidth(175);

        sellBtn = new Button("sprzedaj");
        sellBtn.setMinWidth(175);

        inspectorBox.getChildren().addAll(inspectorTitleLabel, inspectorStatsLabel, upgradeBtn, sellBtn);
        
        startWaveBtn = new Button("rozpocznij fale 1");
        startWaveBtn.setMinWidth(195);

        speedBtn = new Button("predkosc: x1");
        speedBtn.setMinWidth(195);
        
        Button backButton = new Button("wroc do menu");
        backButton.setMinWidth(195);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button infoButton = new Button("i");
        infoButton.setFont(Font.font("Serif", FontWeight.BOLD, 18));
        infoButton.setMinSize(42, 42);
        infoButton.setMaxSize(42, 42);
        infoButton.setStyle("-fx-background-radius: 8; -fx-border-color: black; -fx-border-radius: 8; -fx-border-width: 2; -fx-background-color: white; -fx-cursor: hand;");

        HBox bottomControls = new HBox(infoButton);
        bottomControls.setAlignment(Pos.BOTTOM_RIGHT);

        GridPane gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);

        Pane effectsLayer = new Pane();
        effectsLayer.setMouseTransparent(true);
        effectsLayer.setPrefSize(14 * 45, 12 * 45);
        effectsLayer.setMaxSize(14 * 45, 12 * 45);
        effectsLayer.setMinSize(14 * 45, 12 * 45);

        StackPane boardContainer = new StackPane(gameBoard, effectsLayer);
        boardContainer.setPadding(new Insets(0, 40, 0, 40));

        setupLevel(currentLevel, gameBoard, effectsLayer, buyFirewallBtn, buyTowerBtn, buyIdsBtn, rootStack);

        upgradeBtn.setOnAction(e -> {
            if (selectedFirewall != null && selectedFirewall.canUpgrade()) {
                int cost = selectedFirewall.getUpgradeCost();
                if (money >= cost) {
                    money -= cost;
                    moneyLabel.setText("budzet: " + money + " pgCoinow");
                    selectedFirewall.upgrade();
                    updateInspector();
                }
            } else if (selectedTower != null && selectedTower.canUpgrade()) {
                int cost = selectedTower.getUpgradeCost();
                if (money >= cost) {
                    money -= cost;
                    moneyLabel.setText("budzet: " + money + " pgCoinow");
                    selectedTower.upgrade();
                    updateInspector();
                }
            }
        });

        sellBtn.setOnAction(e -> {
            if (selectedFirewall != null) {
                money += selectedFirewall.getSellValue();
                moneyLabel.setText("budzet: " + money + " pgCoinow");
                int fx = selectedFirewall.getX();
                int fy = selectedFirewall.getY();
                gameBoard.getChildren().remove(selectedFirewall.getGraphic());
                activeFirewalls[fy][fx] = null;
                mapGrid[fy][fx] = 1;
                clearSelection();
            } else if (selectedTower != null) {
                money += selectedTower.getSellValue();
                moneyLabel.setText("budzet: " + money + " pgCoinow");
                int tx = selectedTower.getX();
                int ty = selectedTower.getY();
                gameBoard.getChildren().remove(selectedTower.getGraphic());
                activeTowers.remove(selectedTower);
                mapGrid[ty][tx] = 2;
                clearSelection();
            }
        });

        buyFirewallBtn.setOnAction(e -> {
            clearSelection();
            if (buyingType == 1) {
                buyingType = 0;
                buyFirewallBtn.setText("kup firewall (koszt: 50)");
                toggleHighlight(0);
            } else if (money >= 50) {
                buyingType = 1;
                buyFirewallBtn.setText("anuluj");
                buyTowerBtn.setText("kup antywirus (koszt: 75)");
                buyIdsBtn.setText("kup skaner IDS (koszt: 90)");
                toggleHighlight(1);
            }
        });

        buyTowerBtn.setOnAction(e -> {
            clearSelection();
            if (buyingType == 2) {
                buyingType = 0;
                buyTowerBtn.setText("kup antywirus (koszt: 75)");
                toggleHighlight(0);
            } else if (money >= 75) {
                buyingType = 2;
                buyTowerBtn.setText("anuluj");
                buyFirewallBtn.setText("kup firewall (koszt: 50)");
                buyIdsBtn.setText("kup skaner IDS (koszt: 90)");
                toggleHighlight(2);
            }
        });

        buyIdsBtn.setOnAction(e -> {
            clearSelection();
            if (buyingType == 3) {
                buyingType = 0;
                buyIdsBtn.setText("kup skaner IDS (koszt: 90)");
                toggleHighlight(0);
            } else if (money >= 90) {
                buyingType = 3;
                buyIdsBtn.setText("anuluj");
                buyFirewallBtn.setText("kup firewall (koszt: 50)");
                buyTowerBtn.setText("kup antywirus (koszt: 75)");
                toggleHighlight(2);
            }
        });

        startWaveBtn.setOnAction(e -> {
            if (levelCompleted) {
                if (currentLevel < maxLevels) {
                    currentLevel++;
                    money += 100;
                    setupLevel(currentLevel, gameBoard, effectsLayer, buyFirewallBtn, buyTowerBtn, buyIdsBtn, rootStack);
                    processPopups(rootStack);
                }
            } else if (!isWaveActive && waveNumber < levelWaves.size()) {
                currentWaveData = levelWaves.get(waveNumber);
                waveNumber++;
                isWaveActive = true;
                spawnedInCurrentWave = 0;
                
                waveLabel.setText("fala: " + waveNumber + " / " + levelWaves.size());
                startWaveBtn.setText("trwa fala " + waveNumber + "...");
                startWaveBtn.setDisable(true);
            }
        });

        speedBtn.setOnAction(e -> {
            if (gameSpeed == 1.0) {
                gameSpeed = 2.0;
                speedBtn.setText("predkosc: x2");
            } else {
                gameSpeed = 1.0;
                speedBtn.setText("predkosc: x1");
            }
            if (gameLoop != null) {
                gameLoop.setRate(gameSpeed);
            }
        });

        backButton.setOnAction(e -> {
            if (gameLoop != null) gameLoop.stop();
            primaryStage.setScene(menuScene);
        });

        infoButton.setOnAction(e -> {
            if (!isPopupOpen && !discoveredItems.isEmpty()) {
                isPopupOpen = true;
                if (gameLoop != null) gameLoop.pause();
                InfoOverlay.showCarousel(rootStack, discoveredItems, () -> {
                    isPopupOpen = false;
                    if (gameLoop != null) gameLoop.play();
                });
            }
        });

        rightPanel.getChildren().addAll(levelTitleLabel, levelLabel, waveLabel, hpLabel, moneyLabel, buyFirewallBtn, buyTowerBtn, buyIdsBtn, inspectorBox, startWaveBtn, speedBtn, backButton, spacer, bottomControls);
        root.setRight(rightPanel);
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
                        drawLaser(effectsLayer, tower.getX(), tower.getY(), virus.getX(), virus.getY(), tower.getLaserColor());
                        virus.takeTowerDamage(tower.getBaseDamage(), tower.isIds());
                        if (virus.getHp() <= 0) {
                            gameBoard.getChildren().remove(virus.getGraphic());
                            toRemove.add(virus);
                            enemiesNeutralized++;
                            money += virus.getReward();
                            moneyLabel.setText("budzet: " + money + " pgCoinow");
                        }
                        break;
                    }
                }
            }

            for (Virus virus : activeViruses) {
                if (toRemove.contains(virus)) continue;

                int steps = virus.getStepsPerTick();
                int startVx = virus.getX();
                int startVy = virus.getY();
                boolean removedThisTurn = false;

                for (int s = 0; s < steps; s++) {
                    int vx = virus.getX();
                    int vy = virus.getY();

                    if (vx == 11) {
                        gameBoard.getChildren().remove(virus.getGraphic());
                        toRemove.add(virus);
                        removedThisTurn = true;
                        
                        hp -= 10;
                        hpLabel.setText("hp: " + hp);
                        
                        if (hp <= 0) {
                            gameLoop.stop();
                            showEndGameOverlay(rootStack, false, primaryStage, menuScene);
                            return;
                        }
                        break;
                    }

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
                        if (mapGrid[nextY][nextX] == 3 && !virus.isFirewallImmune()) {
                            Firewall fw = activeFirewalls[nextY][nextX];
                            if (fw != null && !fw.isOnCooldown()) {
                                fw.destroyEnemy();
                                gameBoard.getChildren().remove(virus.getGraphic());
                                toRemove.add(virus);
                                enemiesNeutralized++;
                                removedThisTurn = true;
                                break;
                            }
                        }

                        if (nextX > vx) virus.moveRight();
                        else if (nextX < vx) virus.moveLeft();
                        else if (nextY > vy) virus.moveDown();
                        else if (nextY < vy) virus.moveUp();
                    } else {
                        break;
                    }
                }

                if (!removedThisTurn && (virus.getX() != startVx || virus.getY() != startVy)) {
                    int totalDx = virus.getX() - startVx;
                    int totalDy = virus.getY() - startVy;
                    GridPane.setColumnIndex(virus.getGraphic(), virus.getX() + 1);
                    GridPane.setRowIndex(virus.getGraphic(), virus.getY());
                    virus.animateMove(totalDx, totalDy, gameSpeed);
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

                        queueDiscovery(type.name(), rootStack);
                    }
                } else if (activeViruses.isEmpty()) {
                    isWaveActive = false;
                    
                    if (waveNumber < levelWaves.size()) {
                        startWaveBtn.setText("rozpocznij fale " + (waveNumber + 1));
                        startWaveBtn.setDisable(false);
                    } else {
                        levelCompleted = true;
                        if (currentLevel < maxLevels) {
                            startWaveBtn.setText("przejdz do poziomu " + (currentLevel + 1));
                            startWaveBtn.setDisable(false);
                        } else {
                            gameLoop.stop();
                            showEndGameOverlay(rootStack, true, primaryStage, menuScene);
                        }
                    }
                }
            }

            updateVirusStacks();
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.setRate(gameSpeed);
        gameLoop.play();

        processPopups(rootStack);

        return new Scene(rootStack, 1000, 700);
    }

    private void selectFirewall(Firewall fw) {
        selectedFirewall = fw;
        selectedTower = null;
        updateInspector();
    }

    private void selectTower(Antivirus tower) {
        selectedTower = tower;
        selectedFirewall = null;
        updateInspector();
    }

    private void clearSelection() {
        selectedFirewall = null;
        selectedTower = null;
        inspectorBox.setVisible(false);
        inspectorBox.setManaged(false);
    }

    private void updateInspector() {
        if (selectedFirewall != null) {
            inspectorBox.setVisible(true);
            inspectorBox.setManaged(true);
            inspectorTitleLabel.setText("firewall (poz. " + selectedFirewall.getLevel() + ")");
            inspectorStatsLabel.setText("blokady: " + selectedFirewall.getMaxBlocks() + " | odnowienie: " + selectedFirewall.getCooldownDuration() + "s");
            
            if (selectedFirewall.canUpgrade()) {
                upgradeBtn.setText("ulepsz (" + selectedFirewall.getUpgradeCost() + " pgC)");
                upgradeBtn.setDisable(false);
            } else {
                upgradeBtn.setText("maks. poziom");
                upgradeBtn.setDisable(true);
            }
            sellBtn.setText("sprzedaj (+" + selectedFirewall.getSellValue() + " pgC)");
        } else if (selectedTower != null) {
            inspectorBox.setVisible(true);
            inspectorBox.setManaged(true);
            String name = selectedTower.isIds() ? "skaner IDS" : "antywirus";
            inspectorTitleLabel.setText(name + " (poz. " + selectedTower.getLevel() + ")");
            inspectorStatsLabel.setText("obrazenia: " + selectedTower.getBaseDamage() + " | zasieg: " + selectedTower.getRange());
            
            if (selectedTower.canUpgrade()) {
                upgradeBtn.setText("ulepsz (" + selectedTower.getUpgradeCost() + " pgC)");
                upgradeBtn.setDisable(false);
            } else {
                upgradeBtn.setText("maks. poziom");
                upgradeBtn.setDisable(true);
            }
            sellBtn.setText("sprzedaj (+" + selectedTower.getSellValue() + " pgC)");
        }
    }

    private void showEndGameOverlay(StackPane rootStack, boolean victory, Stage primaryStage, Scene menuScene) {
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");

        VBox card = new VBox(18);
        card.setPadding(new Insets(35));
        card.setPrefSize(520, 320);
        card.setMaxSize(520, 320);
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + (victory ? "#2b8a3e" : "#c92a2a") + ";" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 3;"
        );

        Label title = new Label(victory ? "system zabezpieczony" : "naruszenie bezpieczeństwa");
        title.setFont(Font.font("System", FontWeight.BOLD, 26));
        title.setTextFill(victory ? Color.DARKGREEN : Color.FIREBRICK);

        Label subtitle = new Label(victory
            ? "wszystkie sieci zostały skutecznie obronione przed atakiem."
            : "zbyt wiele złośliwych pakietów przedostało się do serwera.");
        subtitle.setWrapText(true);
        subtitle.setFont(Font.font("System", 14));

        Label stats = new Label(
            "osiągnięty poziom: " + currentLevel + " / " + maxLevels +
            "\nzneutralizowane zagrożenia: " + enemiesNeutralized +
            "\npozostałe hp serwera: " + Math.max(0, hp)
        );
        stats.setFont(Font.font("System", FontWeight.BOLD, 14));
        stats.setStyle("-fx-background-color: #f1f3f5; -fx-padding: 12; -fx-background-radius: 8;");

        Button menuBtn = new Button("powrót do menu głównego");
        menuBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        menuBtn.setMinSize(220, 42);
        menuBtn.setStyle("-fx-cursor: hand;");
        menuBtn.setOnAction(e -> primaryStage.setScene(menuScene));

        card.getChildren().addAll(title, subtitle, stats, menuBtn);
        backdrop.getChildren().add(card);
        rootStack.getChildren().add(backdrop);
    }

    private void queueDiscovery(String id, StackPane rootStack) {
        if (!discoveredItems.contains(id)) {
            discoveredItems.add(id);
            pendingDiscoveries.add(id);
            processPopups(rootStack);
        }
    }

    private void processPopups(StackPane rootStack) {
        if (isPopupOpen) return;

        if (pendingLevelIntro > 0) {
            isPopupOpen = true;
            if (gameLoop != null) gameLoop.pause();

            int levelToShow = pendingLevelIntro;
            pendingLevelIntro = 0;

            InfoOverlay.showLevelIntroPopup(rootStack, levelToShow, () -> {
                isPopupOpen = false;
                processPopups(rootStack);
            });
            return;
        }

        if (!pendingDiscoveries.isEmpty()) {
            isPopupOpen = true;
            if (gameLoop != null) gameLoop.pause();

            String nextId = pendingDiscoveries.poll();
            InfoOverlay.CardEntry entry = InfoOverlay.getCardById(nextId);

            InfoOverlay.showDiscoveryPopup(rootStack, entry, () -> {
                isPopupOpen = false;
                processPopups(rootStack);
            });
            return;
        }

        if (gameLoop != null) {
            gameLoop.play();
        }
    }

    private void setupLevel(int level, GridPane gameBoard, Pane effectsLayer, Button buyFirewallBtn, Button buyTowerBtn, Button buyIdsBtn, StackPane rootStack) {
        gameBoard.getChildren().clear();
        effectsLayer.getChildren().clear();
        activeViruses.clear();
        activeTowers.clear();
        activeFirewalls = new Firewall[12][12];
        intersectionState = new int[12][12];
        currentSpawnIndex = 0;
        clearSelection();

        waveNumber = 0;
        isWaveActive = false;
        levelCompleted = false;
        buyingType = 0;

        pendingLevelIntro = level;

        loadMap("/map_level" + level + ".txt");
        loadWaves("/waves_level" + level + ".txt");

        boolean hasTowerTiles = false;
        for (int r = 0; r < 12; r++) {
            for (int c = 0; c < 12; c++) {
                if (mapGrid[r][c] == 2) {
                    hasTowerTiles = true;
                    break;
                }
            }
        }

        queueDiscovery("FW", rootStack);
        if (hasTowerTiles) {
            queueDiscovery("AV", rootStack);
            queueDiscovery("IDS", rootStack);
        }

        InfoOverlay.LevelEntry levelInfo = InfoOverlay.getLevelInfo(level);
        levelTitleLabel.setText(levelInfo.title);
        levelLabel.setText("poziom: " + level + " / " + maxLevels);
        waveLabel.setText("fala: 0 / " + levelWaves.size());
        moneyLabel.setText("budzet: " + money + " pgCoinow");
        buyFirewallBtn.setText("kup firewall (koszt: 50)");
        buyTowerBtn.setText("kup antywirus (koszt: 75)");
        buyIdsBtn.setText("kup skaner IDS (koszt: 90)");
        buyTowerBtn.setDisable(!hasTowerTiles);
        buyIdsBtn.setDisable(!hasTowerTiles);

        if (levelWaves.isEmpty()) {
            startWaveBtn.setText("brak fal w pliku");
            startWaveBtn.setDisable(true);
        } else {
            startWaveBtn.setText("rozpocznij fale 1");
            startWaveBtn.setDisable(false);
        }

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
                        firewall.getGraphic().setOnMouseClicked(ev -> {
                            if (buyingType == 0) selectFirewall(firewall);
                        });
                        activeFirewalls[finalRow][finalCol] = firewall;
                        gameBoard.add(firewall.getGraphic(), finalCol + 1, finalRow);
                    } else if ((buyingType == 2 || buyingType == 3) && mapGrid[finalRow][finalCol] == 2) {
                        boolean isIds = (buyingType == 3);
                        int cost = isIds ? 90 : 75;
                        money -= cost;
                        moneyLabel.setText("budzet: " + money + " pgCoinow");
                        buyingType = 0;
                        buyTowerBtn.setText("kup antywirus (koszt: 75)");
                        buyIdsBtn.setText("kup skaner IDS (koszt: 90)");
                        
                        toggleHighlight(0);
                        mapGrid[finalRow][finalCol] = 4;
                        
                        Antivirus tower = new Antivirus(finalCol, finalRow, isIds);
                        tower.getGraphic().setOnMouseClicked(ev -> {
                            if (buyingType == 0) selectTower(tower);
                        });
                        activeTowers.add(tower);
                        gameBoard.add(tower.getGraphic(), finalCol + 1, finalRow);
                    } else if (buyingType == 0) {
                        clearSelection();
                    }
                });
                
                gameBoard.add(tile, col + 1, row);
            }
        }
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

    private void drawLaser(Pane layer, int fromCol, int fromRow, int toCol, int toRow, Color laserColor) {
        double startX = (fromCol + 1) * 45.0 + 22.5;
        double startY = fromRow * 45.0 + 22.5;
        double endX = (toCol + 1) * 45.0 + 22.5;
        double endY = toRow * 45.0 + 22.5;

        Line laser = new Line(startX, startY, endX, endY);
        laser.setStroke(laserColor);
        laser.setStrokeWidth(3);
        layer.getChildren().add(laser);

        PauseTransition pause = new PauseTransition(Duration.millis(200 / gameSpeed));
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
            System.out.println("nie udalo sie zaladowac pliku mapy: " + path);
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
            System.out.println("nie udalo sie zaladowac pliku fal: " + path);
        }
    }
}