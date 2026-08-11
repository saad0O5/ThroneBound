package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.GameClient;
import network.GameServer;
import persistence.PlayerProfile;

public class HostJoinScreen extends AnchorPane {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;
    private final TextField hostField = new TextField("127.0.0.1");
    private final TextField portField = new TextField("5000");
    private final Label statusLabel = new Label("Choose a role to begin.");

    public HostJoinScreen(ThroneBoundApp app, PlayerProfile profile) {
        this.app = app;
        this.profile = profile;

        getStyleClass().addAll("screen-root", "host-join-screen");

        Button tabHost = new Button("Host");
        Button tabJoin = new Button("Join");
        tabHost.getStyleClass().addAll("tab-button", "action-button");
        tabJoin.getStyleClass().addAll("tab-button", "action-button");
        HBox tabs = new HBox(8, tabHost, tabJoin);
        tabs.setAlignment(Pos.CENTER);
        tabs.setMaxWidth(560);

        Button hostMatchBtn = new Button("Host Match");
        hostMatchBtn.getStyleClass().addAll("action-button", "wide-button");
        hostMatchBtn.setOnAction(e -> hostMatch());
        hostMatchBtn.setMaxWidth(Double.MAX_VALUE);
        hostField.setMaxWidth(Double.MAX_VALUE);
        portField.setMaxWidth(Double.MAX_VALUE);
        VBox hostPane = new VBox(12, new Label("Host IP / Port"), hostField, portField, hostMatchBtn, statusLabel);
        hostPane.getStyleClass().addAll("panel", "host-join-panel");
        hostPane.setMaxWidth(520);
        hostPane.setFillWidth(true);

        Button connectBtn = new Button("Connect");
        connectBtn.getStyleClass().addAll("action-button", "wide-button");
        connectBtn.setOnAction(e -> joinMatch());
        connectBtn.setMaxWidth(Double.MAX_VALUE);
        VBox joinPane = new VBox(12, new Label("Connect to Host"), hostField, portField, connectBtn, statusLabel);
        joinPane.getStyleClass().addAll("panel", "host-join-panel");
        joinPane.setMaxWidth(520);
        joinPane.setFillWidth(true);

        VBox content = new VBox(24, tabs, hostPane, joinPane);
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(560);
        AnchorPane.setTopAnchor(content, 60.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);

        tabHost.setOnAction(e -> {
            hostPane.setVisible(true);
            joinPane.setVisible(false);
            tabHost.getStyleClass().add("active-tab");
            tabJoin.getStyleClass().remove("active-tab");
        });
        tabJoin.setOnAction(e -> {
            hostPane.setVisible(false);
            joinPane.setVisible(true);
            tabJoin.getStyleClass().add("active-tab");
            tabHost.getStyleClass().remove("active-tab");
        });

        // default to Host mode
        hostPane.setVisible(true);
        joinPane.setVisible(false);
        tabHost.getStyleClass().add("active-tab");

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(e -> app.showMainMenu(profile));
        AnchorPane.setTopAnchor(backButton, 12.0);
        AnchorPane.setLeftAnchor(backButton, 12.0);

        getChildren().addAll(content, backButton);
    }

    private void hostMatch() {
        String hostText = hostField.getText().trim();
        String portText = portField.getText().trim();
        if (hostText.isEmpty() || portText.isEmpty()) {
            statusLabel.setText("Host and port fields must not be empty.");
            return;
        }
        try {
            int port = Integer.parseInt(portText);
            GameServer server = new GameServer(port);
            server.setHasLocalHost(true);
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
        String hostText = hostField.getText().trim();
        String portText = portField.getText().trim();
        if (hostText.isEmpty() || portText.isEmpty()) {
            statusLabel.setText("Host and port fields must not be empty.");
            return;
        }
        try {
            int port = Integer.parseInt(portText);
            GameClient client = new GameClient();
            client.connect(hostText, port);
            client.listen();
            app.setActiveClient(client);
            statusLabel.setText("Connected to " + hostText + ":" + port);
            app.showDeckBuilder(profile);
        } catch (NumberFormatException ex) {
            statusLabel.setText("Please enter a valid port number.");
        } catch (RuntimeException ex) {
            statusLabel.setText("Connection failed: " + ex.getMessage());
        }
    }
}
