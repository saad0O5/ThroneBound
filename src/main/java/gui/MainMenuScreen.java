package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import persistence.MatchRecord;
import persistence.PlayerProfile;

public class MainMenuScreen extends VBox {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;

    public MainMenuScreen(ThroneBoundApp app, PlayerProfile profile) {
        this.app = app;
        this.profile = profile;

        setAlignment(Pos.CENTER);
        setSpacing(14);
        setPadding(new Insets(32));
        getStyleClass().add("screen-root");

        Label title = new Label("Welcome, " + profile.getUsername());
        title.getStyleClass().add("title-label");

        Label subtitle = new Label("Choose your next move in the realm");
        subtitle.getStyleClass().add("subtitle-label");

        int wins = 0;
        int losses = 0;
        for (MatchRecord record : profile.getMatchHistory().getRecords()) {
            String result = record.getResult().toLowerCase();
            if (result.contains("win")) {
                wins++;
            } else if (result.contains("loss")) {
                losses++;
            }
        }

        Label statsLabel = new Label("Wins: " + wins + " | Losses: " + losses);
        statsLabel.getStyleClass().add("info-label");

        Button playButton = new Button("Play");
        Button deckButton = new Button("Deck Builder");
        Button refreshButton = new Button("Refresh Stats");
        Button exitButton = new Button("Exit");

        playButton.setOnAction(event -> app.showHostJoin(profile));
        deckButton.setOnAction(event -> app.showDeckBuilder(profile));
        refreshButton.setOnAction(event -> app.showMainMenu(profile));
        exitButton.setOnAction(event -> app.close());

        playButton.getStyleClass().add("action-button");
        deckButton.getStyleClass().add("secondary-button");
        refreshButton.getStyleClass().add("secondary-button");
        exitButton.getStyleClass().add("danger-button");

        VBox menuBox = new VBox(10, playButton, deckButton, refreshButton, exitButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getStyleClass().add("panel");
        menuBox.setMaxWidth(360);

        Label instructionHeader = new Label("How to Play");
        instructionHeader.getStyleClass().add("subtitle-label");
        Label instructions = new Label("Build a deck of 25 cards, then play creatures and spells in lanes. \nDestroy opponent creatures or reduce their life to 0 to win. Use cards wisely and watch your resources.");
        instructions.setWrapText(true);
        instructions.getStyleClass().add("info-label");

        VBox helpPanel = new VBox(8, instructionHeader, instructions);
        helpPanel.getStyleClass().add("panel");
        helpPanel.setMaxWidth(360);

        getChildren().addAll(title, subtitle, statsLabel, menuBox, helpPanel);
    }
}
