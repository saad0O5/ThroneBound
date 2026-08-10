package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import network.GameClient;
import network.GameServer;
import persistence.PlayerProfile;

public class HostJoinScreen extends VBox {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;
    private final TextField hostField = new TextField("127.0.0.1");
    private final TextField portField = new TextField("5000");
    private final Label statusLabel = new Label("Choose a role to begin.");

    public HostJoinScreen(ThroneBoundApp app, PlayerProfile profile) {
        this.app = app;
        this.profile = profile;

        setAlignment(Pos.CENTER);
        setSpacing(12);
        setPadding(new Insets(32));
        getStyleClass().add("screen-root");

        Label title = new Label("Host or Join a Match");
        title.getStyleClass().add("title-label");

        Label subtitle = new Label("Gather allies or face your rival across the network");
        subtitle.getStyleClass().add("subtitle-label");

        Button hostButton = new Button("Host Match");
        Button joinButton = new Button("Join Match");
        Button backButton = new Button("Back");

        hostButton.setOnAction(event -> hostMatch());
        joinButton.setOnAction(event -> joinMatch());
        backButton.setOnAction(event -> app.showMainMenu(profile));
        hostButton.getStyleClass().add("action-button");
        joinButton.getStyleClass().add("secondary-button");
        backButton.getStyleClass().add("secondary-button");

        hostField.setMaxWidth(320);
        portField.setMaxWidth(320);
        statusLabel.getStyleClass().add("status-label");

        VBox formBox = new VBox(10, title, subtitle, new Label("Host IP / Port"), hostField, portField, hostButton, joinButton, backButton, statusLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.getStyleClass().add("panel");
        formBox.setMaxWidth(420);
        getChildren().add(formBox);
    }

    private void hostMatch() {
        try {
            int port = Integer.parseInt(portField.getText());
            GameServer server = new GameServer(port);
            app.setActiveServer(server);
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (RuntimeException ex) {
                    Platform.runLater(() -> statusLabel.setText("Host failed: " + ex.getMessage()));
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();
            statusLabel.setText("Hosting on port " + port + "... waiting for opponent.");
            app.showDeckBuilder(profile);
        } catch (NumberFormatException ex) {
            statusLabel.setText("Please enter a valid port number.");
        }
    }

    private void joinMatch() {
        try {
            int port = Integer.parseInt(portField.getText());
            GameClient client = new GameClient();
            client.connect(hostField.getText(), port);
            client.listen();
            app.setActiveClient(client);
            statusLabel.setText("Connected to " + hostField.getText() + ":" + port);
            app.showDeckBuilder(profile);
        } catch (RuntimeException ex) {
            statusLabel.setText("Connection failed: " + ex.getMessage());
        }
    }
}
