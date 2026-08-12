package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import persistence.MatchRecord;
import persistence.PlayerProfile;

public class MainMenuScreen extends AnchorPane {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;

    public MainMenuScreen(ThroneBoundApp app, PlayerProfile profile) {
        this.app = app;
        this.profile = profile;

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

        VBox menuBox = new VBox(14, playButton, deckButton, refreshButton, exitButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getStyleClass().add("panel");
        menuBox.setMaxWidth(420);
        menuBox.setMinWidth(360);

        Label instructionHeader = new Label("How to Play");
        instructionHeader.getStyleClass().add("subtitle-label");
        Label instructions = new Label("Build a deck of 12 cards, then play creatures and spells in lanes.\nGoal: reduce the enemy life to 0. If no one reaches 0, the match ends after 30 turns by higher life total.\nEnd your turn to pass; use your resources carefully and always check the board before acting.");
        instructions.setWrapText(true);
        instructions.setMaxWidth(520);
        instructions.getStyleClass().add("info-label");

        VBox helpPanel = new VBox(10, instructionHeader, instructions);
        helpPanel.getStyleClass().add("panel");
        helpPanel.setMaxWidth(520);
        helpPanel.setMinWidth(420);

        VBox centerColumn = new VBox(24, menuBox, helpPanel);
        centerColumn.setAlignment(Pos.TOP_CENTER);
        centerColumn.setMaxWidth(520);

        VBox titleColumn = new VBox(8, title, subtitle);
        titleColumn.setAlignment(Pos.CENTER);
        titleColumn.setMaxWidth(760);

        VBox content = new VBox(28, titleColumn, centerColumn);
        content.setAlignment(Pos.TOP_CENTER);
        content.getStyleClass().add("menu-content");
        content.setMaxWidth(760);
        content.setPrefWidth(760);

        AnchorPane.setTopAnchor(content, 90.0);
        AnchorPane.setLeftAnchor(content, 120.0);
        AnchorPane.setRightAnchor(content, 120.0);

        AnchorPane.setTopAnchor(statsLabel, 18.0);
        AnchorPane.setRightAnchor(statsLabel, 24.0);

        getChildren().addAll(content, statsLabel);
    }
}
