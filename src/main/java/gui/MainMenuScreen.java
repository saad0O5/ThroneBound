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
        setSpacing(12);
        setPadding(new Insets(24));

        Label title = new Label("Welcome, " + profile.getUsername());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

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
        statsLabel.setStyle("-fx-font-size: 14px;");

        Button playButton = new Button("Play");
        Button deckButton = new Button("Deck Builder");
        Button statsButton = new Button("Profile / Stats");
        Button exitButton = new Button("Exit");

        playButton.setOnAction(event -> app.showHostJoin(profile));
        deckButton.setOnAction(event -> app.showDeckBuilder(profile));
        statsButton.setOnAction(event -> app.showMainMenu(profile));
        exitButton.setOnAction(event -> app.close());

        getChildren().addAll(title, statsLabel, playButton, deckButton, statsButton, exitButton);
    }
}
