package jpwp;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class InfoOverlay {

    public static class CardEntry {
        String id;
        String badge;
        Node icon;
        String title;
        String stats;
        String role;
        String pros;
        String cons;
        String realWorld;

        public CardEntry(String id, String badge, Node icon, String title, String stats, String role, String pros, String cons, String realWorld) {
            this.id = id;
            this.badge = badge;
            this.icon = icon;
            this.title = title;
            this.stats = stats;
            this.role = role;
            this.pros = pros;
            this.cons = cons;
            this.realWorld = realWorld;
        }
    }

    public static List<CardEntry> getAllCards() {
        List<CardEntry> list = new ArrayList<>();

        Circle virusIcon = new Circle(28, Color.GREEN);
        virusIcon.setStroke(Color.DARKGREEN);
        virusIcon.setStrokeWidth(3);
        list.add(new CardEntry(
            "V", "nowy przeciwnik", virusIcon,
            "virus (kod: V)",
            "hp: 30 | nagroda: 15 pgCoinów",
            "podstawowy złośliwy program próbujący przedostać się przez sieć do serwera.",
            "często występuje w dużych, gęstych grupach na ścieżce.",
            "ginie natychmiast na aktywnym firewallu; otrzymuje 100% obrażeń od antywirusa.",
            "w rzeczywistości: klasyczny wirus komputerowy replikujący się w plikach, łatwy do wykrycia przez podstawowe sygnatury i filtrowanie ruchu."
        ));

        Circle trojanIcon = new Circle(28, Color.DARKORANGE);
        trojanIcon.setStroke(Color.BROWN);
        trojanIcon.setStrokeWidth(3);
        list.add(new CardEntry(
            "T", "nowy przeciwnik", trojanIcon,
            "trojan (kod: T)",
            "hp: 60 | nagroda: 25 pgCoinów",
            "zamaskowany pakiet danych udający bezpieczny ruch sieciowy.",
            "całkowicie omija firewall oraz blokuje 50% obrażeń od antywirusa.",
            "rzadziej występuje w falach ze względu na większą złożoność ataku.",
            "w rzeczywistości: koń trojański podszywa się pod legalne oprogramowanie, dzięki czemu przechodzi przez zaporę sieciową (firewall) jako zaufany ruch."
        ));

        Rectangle fwIcon = new Rectangle(52, 52, Color.RED);
        fwIcon.setStroke(Color.BLACK);
        fwIcon.setStrokeType(StrokeType.INSIDE);
        fwIcon.setStrokeWidth(2);
        list.add(new CardEntry(
            "FW", "nowa wieżyczka", fwIcon,
            "firewall (koszt: 50 pgCoinów)",
            "pojemność: 5 blokad | czas odnowienia: 5s",
            "zapora sieciowa stawiana bezpośrednio na trasie przesyłu danych.",
            "natychmiastowo neutralizuje podstawowe wirusy bez względu na ich hp.",
            "przeciąża się po 5 blokadach i jest bezradny wobec zamaskowanych trojanów.",
            "w rzeczywistości: zapora sieciowa filtrująca pakiety na podstawie reguł (np. portów i adresów ip), może zostać zapchana przy zbyt dużym ruchu."
        ));

        StackPane avIcon = new StackPane();
        Circle outer = new Circle(28, Color.NAVY);
        outer.setStroke(Color.CYAN);
        outer.setStrokeWidth(4);
        Circle inner = new Circle(11, Color.CYAN);
        avIcon.getChildren().addAll(outer, inner);
        list.add(new CardEntry(
            "AV", "nowa wieżyczka", avIcon,
            "antywirus (koszt: 75 pgCoinów)",
            "obrażenia: 10 | zasięg: 2 pola",
            "aktywny skaner analizujący ruch w promieniu dwóch kratek wokół węzła.",
            "nie przeciąża się i potrafi wykryć oraz zniszczyć ukryte trojany.",
            "wymaga czasu na pełne przeskanowanie i usunięcie zagrożenia.",
            "w rzeczywistości: oprogramowanie antywirusowe (edr), które analizuje zachowanie plików i procesów wewnątrz systemu, wykrywając nawet ukryte zagrożenia."
        ));

        return list;
    }

    public static CardEntry getCardById(String id) {
        for (CardEntry entry : getAllCards()) {
            if (entry.id.equals(id)) return entry;
        }
        return null;
    }

    public static void showCarousel(StackPane rootStack, Runnable onClose) {
        List<CardEntry> cards = getAllCards();
        final int[] currentIndex = {0};

        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.78);");
        backdrop.setPadding(new Insets(40));

        StackPane cardHolder = new StackPane();
        Label counterLabel = new Label("1 / " + cards.size());
        counterLabel.setTextFill(Color.WHITE);
        counterLabel.setFont(Font.font("System", FontWeight.BOLD, 15));

        Button prevBtn = new Button("<");
        prevBtn.setFont(Font.font("System", FontWeight.BOLD, 22));
        prevBtn.setMinSize(55, 120);
        prevBtn.setStyle("-fx-background-radius: 10; -fx-cursor: hand;");

        Button nextBtn = new Button(">");
        nextBtn.setFont(Font.font("System", FontWeight.BOLD, 22));
        nextBtn.setMinSize(55, 120);
        nextBtn.setStyle("-fx-background-radius: 10; -fx-cursor: hand;");

        Runnable refreshCard = () -> {
            cardHolder.getChildren().setAll(buildCardView(cards.get(currentIndex[0]), false));
            counterLabel.setText((currentIndex[0] + 1) + " / " + cards.size());
            prevBtn.setDisable(currentIndex[0] == 0);
            nextBtn.setDisable(currentIndex[0] == cards.size() - 1);
        };

        prevBtn.setOnAction(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                refreshCard.run();
            }
        });

        nextBtn.setOnAction(e -> {
            if (currentIndex[0] < cards.size() - 1) {
                currentIndex[0]++;
                refreshCard.run();
            }
        });

        HBox centerRow = new HBox(25, prevBtn, cardHolder, nextBtn);
        centerRow.setAlignment(Pos.CENTER);

        Button closeBtn = new Button("wróć do gry");
        closeBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        closeBtn.setMinSize(180, 42);
        closeBtn.setStyle("-fx-background-radius: 8; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> {
            rootStack.getChildren().remove(backdrop);
            if (onClose != null) onClose.run();
        });

        VBox container = new VBox(20, counterLabel, centerRow, closeBtn);
        container.setAlignment(Pos.CENTER);
        backdrop.getChildren().add(container);

        refreshCard.run();
        rootStack.getChildren().add(backdrop);
    }

    public static void showDiscoveryPopup(StackPane rootStack, CardEntry entry, Runnable onClose) {
        if (entry == null) {
            if (onClose != null) onClose.run();
            return;
        }

        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.82);");
        backdrop.setPadding(new Insets(40));

        VBox cardView = buildCardView(entry, true);

        Button continueBtn = new Button("zrozumiałem, wznów grę");
        continueBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        continueBtn.setMinSize(220, 45);
        continueBtn.setStyle("-fx-background-color: #2b8a3e; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        continueBtn.setOnAction(e -> {
            rootStack.getChildren().remove(backdrop);
            if (onClose != null) onClose.run();
        });

        VBox container = new VBox(25, cardView, continueBtn);
        container.setAlignment(Pos.CENTER);
        backdrop.getChildren().add(container);

        rootStack.getChildren().add(backdrop);
    }

    private static VBox buildCardView(CardEntry entry, boolean showDiscoveryBadge) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(30));
        card.setPrefSize(660, 440);
        card.setMinSize(660, 440);
        card.setMaxSize(660, 440);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: #adb5bd;" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 2;"
        );

        if (showDiscoveryBadge) {
            Label badgeLabel = new Label(entry.badge);
            badgeLabel.setPadding(new Insets(5, 14, 5, 14));
            badgeLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
            badgeLabel.setStyle("-fx-background-color: #fff3bf; -fx-text-fill: #d9480f; -fx-background-radius: 6; -fx-border-color: #ffc078; -fx-border-radius: 6;");
            card.getChildren().add(badgeLabel);
        }

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label titleLabel = new Label(entry.title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));

        Label statsLabel = new Label(entry.stats);
        statsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        statsLabel.setTextFill(Color.DARKSLATEGRAY);

        titleBox.getChildren().addAll(titleLabel, statsLabel);
        header.getChildren().addAll(entry.icon, titleBox);

        Label roleLabel = new Label(entry.role);
        roleLabel.setWrapText(true);
        roleLabel.setFont(Font.font("System", 15));

        Label prosLabel = new Label("+ mocne strony: " + entry.pros);
        prosLabel.setWrapText(true);
        prosLabel.setTextFill(Color.DARKGREEN);
        prosLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label consLabel = new Label("- słabości: " + entry.cons);
        consLabel.setWrapText(true);
        consLabel.setTextFill(Color.FIREBRICK);
        consLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label realWorldLabel = new Label(entry.realWorld);
        realWorldLabel.setWrapText(true);
        realWorldLabel.setPadding(new Insets(12));
        realWorldLabel.setFont(Font.font("System", 13));
        realWorldLabel.setStyle(
            "-fx-background-color: #f1f3f5;" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: #343a40;"
        );

        card.getChildren().addAll(header, roleLabel, prosLabel, consLabel, spacer, realWorldLabel);
        return card;
    }
}