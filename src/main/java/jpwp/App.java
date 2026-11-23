package jpwp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox menuLayout = new VBox(18);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("firewall breach");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 36));

        Scene menuScene = new Scene(menuLayout, 1000, 700);

        Button playBtn = new Button("graj (kampania)");
        playBtn.setMinWidth(240);
        playBtn.setMinHeight(40);
        playBtn.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
        playBtn.setOnAction(e -> {
            GameScreen gameScreen = new GameScreen();
            primaryStage.setScene(gameScreen.getScene(primaryStage, menuScene, 1));
        });

        Button selectLevelToggleBtn = new Button("wybierz poziom");
        selectLevelToggleBtn.setMinWidth(240);
        selectLevelToggleBtn.setMinHeight(40);
        selectLevelToggleBtn.setStyle("-fx-cursor: hand;");

        VBox levelsBox = new VBox(10);
        levelsBox.setAlignment(Pos.CENTER);
        levelsBox.setVisible(false);
        levelsBox.setManaged(false);

        Button lvl1Btn = new Button("1. sieć domowa (niska)");
        lvl1Btn.setMinWidth(240);
        lvl1Btn.setOnAction(e -> {
            GameScreen gameScreen = new GameScreen();
            primaryStage.setScene(gameScreen.getScene(primaryStage, menuScene, 1));
        });

        Button lvl2Btn = new Button("2. sieć małej firmy (średnia)");
        lvl2Btn.setMinWidth(240);
        lvl2Btn.setOnAction(e -> {
            GameScreen gameScreen = new GameScreen();
            primaryStage.setScene(gameScreen.getScene(primaryStage, menuScene, 2));
        });

        Button lvl3Btn = new Button("3. sieć korporacji (wysoka)");
        lvl3Btn.setMinWidth(240);
        lvl3Btn.setOnAction(e -> {
            GameScreen gameScreen = new GameScreen();
            primaryStage.setScene(gameScreen.getScene(primaryStage, menuScene, 3));
        });

        levelsBox.getChildren().addAll(lvl1Btn, lvl2Btn, lvl3Btn);

        selectLevelToggleBtn.setOnAction(e -> {
            boolean isCurrentlyVisible = levelsBox.isVisible();
            levelsBox.setVisible(!isCurrentlyVisible);
            levelsBox.setManaged(!isCurrentlyVisible);
            selectLevelToggleBtn.setText(isCurrentlyVisible ? "wybierz poziom" : "ukryj poziomy");
        });

        Button exitBtn = new Button("wyjdź");
        exitBtn.setMinWidth(240);
        exitBtn.setMinHeight(40);
        exitBtn.setStyle("-fx-cursor: hand;");
        exitBtn.setOnAction(e -> primaryStage.close());

        menuLayout.getChildren().addAll(titleLabel, playBtn, selectLevelToggleBtn, levelsBox, exitBtn);

        primaryStage.setTitle("firewall breach");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}