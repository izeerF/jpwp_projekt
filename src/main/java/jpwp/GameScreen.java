package jpwp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameScreen {

    public Scene getScene(Stage primaryStage, Scene menuScene) {
        VBox gameLayout = new VBox(20);
        gameLayout.setAlignment(Pos.CENTER);

        Label infoLabel = new Label("TUTAJ BĘDZIE GRA");
        Button backButton = new Button("Wróć do menu");

        backButton.setOnAction(e -> primaryStage.setScene(menuScene));

        gameLayout.getChildren().addAll(infoLabel, backButton);

        return new Scene(gameLayout, 800, 600);
    }
}