package gui;

import cards.Deck;
import engine.GameState;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import network.GameClient;
import network.GameServer;
import persistence.PlayerProfile;
import persistence.ProfileManager;

import java.util.Optional;

public class ThroneBoundApp extends Application {
    private static final double INITIAL_WIDTH = 1280;
    private static final double INITIAL_HEIGHT = 800;
    private static final double MIN_WIDTH = 1024;
    private static final double MIN_HEIGHT = 720;

    private final ProfileManager profileManager = new ProfileManager("profiles");
    private Stage primaryStage;
    private Scene scene;
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
        stage.setWidth(INITIAL_WIDTH);
        stage.setHeight(INITIAL_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        this.scene = new Scene(new LoginScreen(this, profileManager), INITIAL_WIDTH, INITIAL_HEIGHT);
        // Ensure the initial root participates in the global theme rules
        if (!this.scene.getRoot().getStyleClass().contains("root")) {
            this.scene.getRoot().getStyleClass().add("root");
        }
        this.scene.getStylesheets().add(getClass().getResource("/gui/styles.css").toExternalForm());
        // Programmatically set a scene fill gradient so the window background
        // is painted even if node-level CSS does not apply to the root for
        // any reason on a particular platform or layout.
        LinearGradient sceneGradient = new LinearGradient(
            0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4a3b56")),
            new Stop(0.45, Color.web("#6b4f7a")),
            new Stop(1, Color.web("#3b2b3d"))
        );
        this.scene.setFill(sceneGradient);
        stage.setScene(scene);
        SceneManager.getInstance().initialize(stage, scene);
        centerOnFocusedScreen(stage);
        stage.setOnCloseRequest(event -> {
            if (activeGameState != null && !activeGameState.isMatchOver()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initOwner(stage);
                alert.setTitle("Confirm Quit");
                alert.setHeaderText("Quit current match?");
                alert.setContentText("Are you sure you want to quit? Progress will be lost.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.OK) {
                    event.consume();
                    return;
                }
            }
            close();
        });

        showLogin();
        stage.show();
    }

    private void centerOnFocusedScreen(Stage stage) {
        Rectangle2D bounds = Screen.getScreensForRectangle(0, 0, 1, 1).stream()
                .findFirst()
                .map(Screen::getVisualBounds)
                .orElse(Screen.getPrimary().getVisualBounds());
        stage.setX(bounds.getMinX() + (bounds.getWidth() - INITIAL_WIDTH) / 2);
        stage.setY(bounds.getMinY() + (bounds.getHeight() - INITIAL_HEIGHT) / 2);
    }

    public void showLogin() {
        SceneManager.getInstance().setRoot(new LoginScreen(this, profileManager));
    }

    public void showMainMenu(PlayerProfile profile) {
        SceneManager.getInstance().setRoot(new MainMenuScreen(this, profile));
    }

    public void showHostJoin(PlayerProfile profile) {
        SceneManager.getInstance().setRoot(new HostJoinScreen(this, profile));
    }

    public void showDeckBuilder(PlayerProfile profile) {
        SceneManager.getInstance().setRoot(new DeckBuilderScreen(this, profile));
    }

    public void showMatch(PlayerProfile profile, Deck deck) {
        SceneManager.getInstance().setRoot(new MatchScreen(this, profile, deck));
    }

    public void showResults(PlayerProfile profile, boolean won) {
        SceneManager.getInstance().setRoot(new ResultsScreen(this, profile, won));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
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
