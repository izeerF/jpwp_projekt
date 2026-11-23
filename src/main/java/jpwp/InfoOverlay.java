package jpwp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import javafx.scene.shape.Polygon;
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
            "hp: 30 | prędkość: normalna | nagroda: 15 pgCoinów",
            "podstawowy złośliwy program próbujący przedostać się przez sieć do serwera.",
            "często występuje w dużych, gęstych grupach na ścieżce.",
            "ginie natychmiast na aktywnym firewallu; podatny na antywirus.",
            "w rzeczywistości: klasyczny wirus komputerowy replikujący się w plikach, łatwy do wykrycia przez podstawowe sygnatury i filtrowanie ruchu."
        ));

        Circle trojanIcon = new Circle(28, Color.DARKORANGE);
        trojanIcon.setStroke(Color.BROWN);
        trojanIcon.setStrokeWidth(3);
        list.add(new CardEntry(
            "T", "nowy przeciwnik", trojanIcon,
            "trojan (kod: T)",
            "hp: 45 | prędkość: normalna | nagroda: 25 pgCoinów",
            "zamaskowany pakiet danych udający bezpieczny ruch sieciowy.",
            "całkowicie omija firewall oraz redukuje obrażenia od zwykłego antywirusa.",
            "bardzo podatny na systemy wykrywania włamań (IDS - podwójne obrażenia).",
            "w rzeczywistości: koń trojański podszywa się pod legalne oprogramowanie, dzięki czemu przechodzi przez zaporę sieciową (firewall) jako zaufany ruch."
        ));

        Circle wormIcon = new Circle(28, Color.MAGENTA);
        wormIcon.setStroke(Color.DARKMAGENTA);
        wormIcon.setStrokeWidth(3);
        list.add(new CardEntry(
            "W", "nowy przeciwnik", wormIcon,
            "worm / robak (kod: W)",
            "hp: 40 | prędkość: podwójna (x2) | nagroda: 20 pgCoinów",
            "autonomiczny program błyskawicznie rozprzestrzeniający się po łączach sieciowych.",
            "porusza się dwa razy szybciej i ignoruje skanery IDS (75% redukcji obrażeń).",
            "zostaje zatrzymany przez firewall oraz dostaje zwiększone obrażenia od antywirusa.",
            "w rzeczywistości: robak sieciowy (np. WannaCry) samoczynnie skanuje sieć i wykorzystuje luki w protokołach do błyskawicznego przeskakiwania między węzłami."
        ));

        Circle ransomIcon = new Circle(28, Color.CRIMSON);
        ransomIcon.setStroke(Color.BLACK);
        ransomIcon.setStrokeWidth(3);
        list.add(new CardEntry(
            "R", "nowy przeciwnik", ransomIcon,
            "ransomware (kod: R)",
            "hp: 85 | prędkość: niska | nagroda: 40 pgCoinów",
            "złośliwe oprogramowanie szyfrujące zasoby serwera i blokujące dostęp do plików.",
            "bardzo wysokie hp, omija firewall oraz ignoruje zwykły antywirus i IDS (80% redukcji obrażeń).",
            "bardzo podatny na moduły deszyfrujące (dekryptor - 250% obrażeń).",
            "w rzeczywistości: ataki ransomware (np. LockBit) szyfrują dyski firmowe i żądają okupu; kluczowe jest izolowanie ładunków w piaskownicy (sandbox) i dekrypcja."
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
            "natychmiastowo neutralizuje wirusy oraz szybkie robaki bez względu na ich hp.",
            "przeciąża się po 5 blokadach i jest bezradny wobec trojanów oraz ransomware.",
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
            "aktywny skaner analizujący pliki i ruch w promieniu dwóch kratek wokół węzła.",
            "nie przeciąża się, skuteczny przeciwko wirusom oraz szybkim robakom (120% obrażeń).",
            "słabszy przeciwko trojanom (60% obrażeń) oraz ransomware (20% obrażeń).",
            "w rzeczywistości: oprogramowanie antywirusowe analizujące sygnatury i zachowanie plików wewnątrz stacji roboczych."
        ));

        StackPane idsIcon = new StackPane();
        Rectangle idsBase = new Rectangle(50, 50);
        idsBase.setArcWidth(12);
        idsBase.setArcHeight(12);
        idsBase.setFill(Color.DARKSLATEGRAY);
        idsBase.setStroke(Color.LIME);
        idsBase.setStrokeWidth(3);
        Circle idsCore = new Circle(12, Color.LIME);
        idsIcon.getChildren().addAll(idsBase, idsCore);
        list.add(new CardEntry(
            "IDS", "nowa wieżyczka", idsIcon,
            "skaner IDS (koszt: 90 pgCoinów)",
            "obrażenia: 12 | zasięg: 3 pola",
            "zaawansowany system głębokiej inspekcji pakietów (Deep Packet Inspection) o dużym zasięgu.",
            "demaskuje trojany i zadaje im podwójne obrażenia (200%), duży zasięg 3 kratek.",
            "nieskuteczny przeciwko szybkim robakom, wirusom oraz ransomware.",
            "w rzeczywistości: Intrusion Detection System analizuje anomalie w ruchu sieciowym, wykrywając ukryte tunele i trojany omijające zwykły firewall."
        ));

        StackPane decIcon = new StackPane();
        Polygon decDiamond = new Polygon(
            26.0, 2.0,
            50.0, 26.0,
            26.0, 50.0,
            2.0, 26.0
        );
        decDiamond.setFill(Color.INDIGO);
        decDiamond.setStroke(Color.MEDIUMORCHID);
        decDiamond.setStrokeWidth(3);
        Circle decCore = new Circle(10, Color.PLUM);
        decIcon.getChildren().addAll(decDiamond, decCore);
        list.add(new CardEntry(
            "DEC", "nowa wieżyczka", decIcon,
            "dekryptor (koszt: 110 pgCoinów)",
            "obrażenia: 22 | zasięg: 2 pola",
            "izolowana piaskownica (sandbox) łamiąca klucze szyfrujące złośliwych pakietów.",
            "zadaje krytyczne obrażenia (250%) przeciwko ransomware.",
            "wysoki koszt budowy i niska skuteczność przeciwko zwykłym wirusom oraz robakom.",
            "w rzeczywistości: systemy Sandbox i narzędzia deszyfrujące (np. No More Ransom) analizują algorytmy szyfrujące i neutralizują ataki ransomware."
        ));

        return list;
    }

    public static CardEntry getCardById(String id) {
        for (CardEntry entry : getAllCards()) {
            if (entry.id.equals(id)) return entry;
        }
        return null;
    }

    public static void showCarousel(StackPane rootStack, Set<String> discoveredIds, Runnable onClose) {
        List<CardEntry> cards = new ArrayList<>();
        for (CardEntry entry : getAllCards()) {
            if (discoveredIds.contains(entry.id)) {
                cards.add(entry);
            }
        }

        if (cards.isEmpty()) {
            if (onClose != null) onClose.run();
            return;
        }

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

    public static class LevelEntry {
        int levelNumber;
        String title;
        String difficulty;
        String difficultyColor;
        String description;

        public LevelEntry(int levelNumber, String title, String difficulty, String difficultyColor, String description) {
            this.levelNumber = levelNumber;
            this.title = title;
            this.difficulty = difficulty;
            this.difficultyColor = difficultyColor;
            this.description = description;
        }
    }

    public static LevelEntry getLevelInfo(int level) {
        switch (level) {
            case 1:
                return new LevelEntry(
                    1,
                    "sieć domowa",
                    "trudność: niska",
                    "#2b8a3e",
                    "prosty router domowy z jednym głównym łączem dostępowym. podstawowa ochrona przed prostymi wirusami i pierwszymi próbami włamań."
                );
            case 2:
                return new LevelEntry(
                    2,
                    "sieć małej firmy",
                    "trudność: średnia",
                    "#e67700",
                    "rozbudowana sieć lokalna z kilkoma punktami styku z internetem. pojawiają się zamaskowane trojany oraz szybkie robaki sieciowe."
                );
            case 3:
            default:
                return new LevelEntry(
                    3,
                    "sieć korporacji",
                    "trudność: wysoka",
                    "#c92a2a",
                    "złożona infrastruktura serwerowa z wieloma węzłami, rozgałęzieniami ruchu oraz atakami ransomware. wymaga pełnej synchronizacji firewalli, antywirusów, skanerów IDS i dekryptorów."
                );
        }
    }

    public static void showLevelIntroPopup(StackPane rootStack, int level, Runnable onClose) {
        LevelEntry info = getLevelInfo(level);

        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.82);");
        backdrop.setPadding(new Insets(40));

        VBox card = new VBox(18);
        card.setPadding(new Insets(35));
        card.setPrefSize(620, 340);
        card.setMinSize(620, 340);
        card.setMaxSize(620, 340);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: #adb5bd;" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 2;"
        );

        HBox topBadges = new HBox(12);
        topBadges.setAlignment(Pos.CENTER_LEFT);

        Label levelBadge = new Label("poziom " + info.levelNumber);
        levelBadge.setPadding(new Insets(5, 14, 5, 14));
        levelBadge.setFont(Font.font("System", FontWeight.BOLD, 13));
        levelBadge.setStyle("-fx-background-color: #e7f5ff; -fx-text-fill: #1864ab; -fx-background-radius: 6;");

        Label diffBadge = new Label(info.difficulty);
        diffBadge.setPadding(new Insets(5, 14, 5, 14));
        diffBadge.setFont(Font.font("System", FontWeight.BOLD, 13));
        diffBadge.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: " + info.difficultyColor + "; -fx-background-radius: 6; -fx-border-color: " + info.difficultyColor + "; -fx-border-radius: 6;");

        topBadges.getChildren().addAll(levelBadge, diffBadge);

        Label titleLabel = new Label(info.title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label descLabel = new Label(info.description);
        descLabel.setWrapText(true);
        descLabel.setFont(Font.font("System", 16));
        descLabel.setStyle("-fx-text-fill: #343a40;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(topBadges, titleLabel, descLabel, spacer);

        Button startBtn = new Button("rozpocznij obronę");
        startBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        startBtn.setMinSize(220, 45);
        startBtn.setStyle("-fx-background-color: #1864ab; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        startBtn.setOnAction(e -> {
            rootStack.getChildren().remove(backdrop);
            if (onClose != null) onClose.run();
        });

        VBox container = new VBox(25, card, startBtn);
        container.setAlignment(Pos.CENTER);
        backdrop.getChildren().add(container);

        rootStack.getChildren().add(backdrop);
    }
}