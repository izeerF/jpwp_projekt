package jpwp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private Scene menuScene;
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Firewall Breach");

        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("FIREWALL BREACH");
        Button startButton = new Button("Graj");
        Button exitButton = new Button("Wyjdź");

        menuScene = new Scene(menuLayout, 800, 600);

        exitButton.setOnAction(e -> primaryStage.close());
        
        startButton.setOnAction(e -> {
            GameScreen gameScreen = new GameScreen();
            primaryStage.setScene(gameScreen.getScene(primaryStage, menuScene));
        });

        menuLayout.getChildren().addAll(titleLabel, startButton, exitButton);
        
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}