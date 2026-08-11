package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import persistence.MatchRecord;
import persistence.PlayerProfile;
import persistence.ProfileManager;
import persistence.UnlockManager;

public class ResultsScreen extends AnchorPane {
    public ResultsScreen(ThroneBoundApp app, PlayerProfile profile, boolean won) {
        getStyleClass().add("screen-root");

        Label title = new Label(won ? "Victory!" : "Defeat");
        title.getStyleClass().add("title-label");
        AnchorPane.setTopAnchor(title, 120.0);
        AnchorPane.setLeftAnchor(title, 0.0);
        AnchorPane.setRightAnchor(title, 0.0);
        title.setAlignment(Pos.CENTER);

        String resultText = won ? "You won the match." : "You lost the match.";
        Label summary = new Label(resultText);
        summary.getStyleClass().add("info-label");
        AnchorPane.setTopAnchor(summary, 260.0);
        AnchorPane.setLeftAnchor(summary, 360.0);

        UnlockManager unlockManager = new UnlockManager();
        unlockManager.checkUnlocks(profile, won);

        Label unlockLabel = new Label(won ? "A new card was unlocked for your profile." : "No new unlocks this time.");
        AnchorPane.setTopAnchor(unlockLabel, 260.0);
        AnchorPane.setLeftAnchor(unlockLabel, 760.0);

        profile.getMatchHistory().addRecord(new MatchRecord("Opponent", won ? "Win" : "Loss"));

        ProfileManager profileManager = new ProfileManager("profiles");
        profileManager.save(profile);

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> app.showMainMenu(profile));
        backButton.getStyleClass().add("action-button");
        UiHelpers.applyHoverEffect(backButton);
        AnchorPane.setBottomAnchor(backButton, 24.0);
        AnchorPane.setLeftAnchor(backButton, 560.0);

        getChildren().addAll(title, summary, unlockLabel, backButton);
    }
}
