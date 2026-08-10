package gui;

import cards.Deck;
import engine.GameState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import network.GameClient;
import network.GameServer;
import persistence.PlayerProfile;
import persistence.ProfileManager;

public class ThroneBoundApp extends Application {
    private final ProfileManager profileManager = new ProfileManager("profiles");
    private Stage primaryStage;
    private GameServer activeServer;
    private GameClient activeClient;
    private GameState activeGameState;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Thronebound");
        showLogin();
        stage.show();
    }

    public void showLogin() {
        primaryStage.setScene(new Scene(new LoginScreen(this, profileManager), 900, 650));
    }

    public void showMainMenu(PlayerProfile profile) {
        primaryStage.setScene(new Scene(new MainMenuScreen(this, profile), 900, 650));
    }

    public void showHostJoin(PlayerProfile profile) {
        primaryStage.setScene(new Scene(new HostJoinScreen(this, profile), 900, 650));
    }

    public void showDeckBuilder(PlayerProfile profile) {
        primaryStage.setScene(new Scene(new DeckBuilderScreen(this, profile), 900, 650));
    }

    public void showMatch(PlayerProfile profile, Deck deck) {
        MatchScreen screen = new MatchScreen(this, profile, deck);
        primaryStage.setScene(new Scene(screen, 1200, 800));
    }

    public void showResults(PlayerProfile profile, boolean won) {
        primaryStage.setScene(new Scene(new ResultsScreen(this, profile, won), 900, 650));
    }

    public void setActiveServer(GameServer server) {
        this.activeServer = server;
    }

    public void setActiveClient(GameClient client) {
        this.activeClient = client;
    }

    public void setActiveGameState(GameState state) {
        this.activeGameState = state;
    }

    public GameServer getActiveServer() {
        return activeServer;
    }

    public GameClient getActiveClient() {
        return activeClient;
    }

    public GameState getActiveGameState() {
        return activeGameState;
    }

    public void close() {
        if (activeClient != null) {
            activeClient.disconnect();
        }
        if (activeServer != null) {
            activeServer.stop();
        }
        primaryStage.close();
    }
}
