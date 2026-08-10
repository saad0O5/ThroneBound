package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import persistence.MatchRecord;
import persistence.PlayerProfile;
import persistence.ProfileManager;
import persistence.UnlockManager;

public class ResultsScreen extends VBox {
    public ResultsScreen(ThroneBoundApp app, PlayerProfile profile, boolean won) {
        setAlignment(Pos.CENTER);
        setSpacing(12);
        setPadding(new Insets(24));

        Label title = new Label(won ? "Victory!" : "Defeat");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        String resultText = won ? "You won the match." : "You lost the match.";
        Label summary = new Label(resultText);

        UnlockManager unlockManager = new UnlockManager();
        unlockManager.checkUnlocks(profile, won);

        Label unlockLabel = new Label(won ? "A new card was unlocked for your profile." : "No new unlocks this time.");
        profile.getMatchHistory().addRecord(new MatchRecord("Opponent", won ? "Win" : "Loss"));

        ProfileManager profileManager = new ProfileManager("profiles");
        profileManager.save(profile);

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> app.showMainMenu(profile));

        getChildren().addAll(title, summary, unlockLabel, backButton);
    }
}
