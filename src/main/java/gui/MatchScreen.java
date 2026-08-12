package gui;

import cards.Card;
import cards.Deck;
import engine.GameState;
import engine.MatchResult;
import engine.Player;
import engine.ResourcePool;
import engine.TimedWinCondition;
import engine.TurnManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import network.EndTurnMessage;
import network.ErrorMessage;
import network.GameClient;
import network.GameStateSnapshot;
import network.Message;
import network.PlayCardMessage;
import network.StartMatchMessage;
import network.StateUpdateMessage;
import persistence.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

public class MatchScreen extends AnchorPane implements GameStateObserver {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;
    private final GameState gameState;
    private final GameClient gameClient;
    private final List<Card> hand;
    private final HBox opponentRow = new HBox(12);
    private final HBox playerRow = new HBox(12);
    private final HBox handRow = new HBox(16);
    private final Label selectedCardLabel = new Label("No card selected.");
    private final Label statusLabel = new Label("Select a card, then choose a lane.");
    private Card selectedCard;
    private Button selectedCardButton = null;
    private Player localPlayer = null;
    private final Label opponentLabel = new Label();
    private final Label turnBanner = new Label("— Turn: " + Player.PLAYER1 + " —");
    private final Label lifeLabel = new Label();
    private final Label resourceLabel = new Label();

    public MatchScreen(ThroneBoundApp app, PlayerProfile profile, Deck deck) {
        this.app = app;
        this.profile = profile;
        // Use the server's authoritative GameState when hosting. When
        // connected as a client, create a local GameState and wait for
        // the server to send StateUpdateMessage snapshots to populate it.
        this.gameClient = app.getActiveClient();
        if (app.getActiveServer() != null) {
            this.gameState = app.getActiveServer().getGameState();
            app.setActiveGameState(this.gameState);
            this.localPlayer = Player.PLAYER1;
        } else if (app.getActiveGameState() != null && this.gameClient == null) {
            this.gameState = app.getActiveGameState();
            this.localPlayer = Player.PLAYER1;
        } else {
            this.gameState = new GameState();
            this.localPlayer = Player.PLAYER1;
        }
        this.hand = new ArrayList<>(deck.getCards().subList(0, Math.min(5, deck.getCards().size())));
        if (this.gameClient != null) {
            this.gameClient.setMessageListener(this::handleNetworkMessage);
        }

        getStyleClass().addAll("screen-root", "match-screen");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(24));
        grid.setVgap(16);
        grid.setHgap(10);
        grid.setPrefSize(1280, 900);
        grid.setMinWidth(1024);
        grid.setMinHeight(920);

        // Row constraints: 8%,22%,6%,22%,8%,26%,8% (total 100)
        double[] heights = {8,22,6,22,8,26,8};
        for (double h : heights) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(h);
            grid.getRowConstraints().add(rc);
        }

        // Single column
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100);
        grid.getColumnConstraints().add(cc);

        opponentRow.setAlignment(Pos.CENTER);
        opponentRow.getStyleClass().addAll("lane-row", "opponent");
        playerRow.setAlignment(Pos.CENTER);
        playerRow.getStyleClass().addAll("lane-row", "player");
        handRow.setAlignment(Pos.CENTER_LEFT);
        handRow.setSpacing(16);

        grid.add(buildTopPanel(), 0, 0);
        grid.add(buildOpponentLanes(), 0, 1);
        grid.add(buildDivider(), 0, 2);
        grid.add(buildPlayerLanes(), 0, 3);
        grid.add(buildPlayerInfoBar(), 0, 4);
        grid.add(buildHandPanel(), 0, 5);
        grid.add(buildStatusStrip(), 0, 6);

        ScrollPane wrapper = new ScrollPane(grid);
        wrapper.setFitToWidth(true);
        wrapper.setFitToHeight(true);
        wrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        wrapper.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        wrapper.getStyleClass().add("match-scrollpane");

        AnchorPane.setTopAnchor(wrapper, 0.0);
        AnchorPane.setBottomAnchor(wrapper, 0.0);
        AnchorPane.setLeftAnchor(wrapper, 0.0);
        AnchorPane.setRightAnchor(wrapper, 0.0);
        getChildren().add(wrapper);

        onStateChanged(gameState);
    }

    @Override
    public void onStateChanged(GameState state) {
        Platform.runLater(() -> {
            renderBoard(state);
            TurnManager turnManager = app.getActiveServer() != null
                    ? app.getActiveServer().getTurnManager()
                    : new TurnManager(new TimedWinCondition());
            if (turnManager.checkWinCondition(state)) {
                MatchResult result = turnManager.determineWinner(state);
                boolean localWon = (localPlayer == Player.PLAYER1 && result == MatchResult.PLAYER1)
                        || (localPlayer == Player.PLAYER2 && result == MatchResult.PLAYER2);
                if (result != MatchResult.ONGOING) {
                    app.showResults(profile, localWon);
                }
            }
        });
    }
    private void handleNetworkMessage(Message message) {
        if (message instanceof network.AssignPlayerMessage assign) {
            this.localPlayer = assign.getAssigned();
            statusLabel.setText("You are " + this.localPlayer + ". Waiting for game state...");
            return;
        }
        if (message instanceof StartMatchMessage) {
            statusLabel.setText("Match started. Waiting for board update...");
            return;
        }
        if (message instanceof ErrorMessage errorMessage) {
            statusLabel.setText("Server error: " + errorMessage.getError());
            return;
        }
        if (message instanceof StateUpdateMessage updateMessage) {
            Object payload = updateMessage.getStatePayload();
            if (payload instanceof GameStateSnapshot snapshot) {
                snapshot.applyTo(gameState);
                onStateChanged(gameState);
            } else {
                statusLabel.setText("Received an invalid game update.");
            }
        }
    }

    private VBox buildTopPanel() {
        Label title = new Label("Match in Progress");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(event -> {
            if (gameClient != null) {
                gameClient.disconnect();
            }
            if (app.getActiveServer() != null) {
                app.getActiveServer().stop();
            }
            app.showMainMenu(profile);
        });
        UiHelpers.applyHoverEffect(backButton);
        HBox topRow = new HBox(18, title, backButton);
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(18);

        selectedCardLabel.getStyleClass().add("info-label");
        Label headerSubtitle = new Label("Match board — choose your cards and dominate the lanes");
        headerSubtitle.getStyleClass().add("subtitle-label");
        Label rulesLabel = new Label("Goal: reduce the enemy to 0 life. End turn to pass. Match ends at 30 turns or when someone hits 0 life.");
        rulesLabel.setWrapText(true);
        rulesLabel.setMaxWidth(980);
        rulesLabel.getStyleClass().add("info-label");
        lifeLabel.getStyleClass().add("info-label");
        updateLifeLabel(gameState);

        HBox statusRow = new HBox(12, selectedCardLabel, lifeLabel);
        statusRow.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(10, topRow, headerSubtitle, rulesLabel, statusRow);
        panel.getStyleClass().addAll("panel", "top-panel");
        panel.setPadding(new Insets(12));
        AnchorPane.setTopAnchor(panel, 0.0);
        AnchorPane.setLeftAnchor(panel, 0.0);
        AnchorPane.setRightAnchor(panel, 0.0);
        return panel;
    }

    private VBox buildOpponentLanes() {
        Player opponent = localPlayer != null && localPlayer == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        opponentLabel.setText("Opponent: " + opponent);
        opponentLabel.getStyleClass().setAll("board-title", "lane-group-label");

        opponentRow.getChildren().clear();
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            opponentRow.getChildren().add(buildLanePanel(i, null, false));
        }

        VBox container = new VBox(10, opponentLabel, opponentRow);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private VBox buildDivider() {
        updateTurnBanner(gameState);
        VBox box = new VBox(turnBanner);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox buildPlayerLanes() {
        Label playerLabel = new Label("Your Lanes");
        playerLabel.getStyleClass().setAll("board-title", "lane-group-label");

        playerRow.getChildren().clear();
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            playerRow.getChildren().add(buildLanePanel(i, null, true));
        }

        VBox container = new VBox(10, playerLabel, playerRow);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private HBox buildPlayerInfoBar() {
        HBox box = new HBox(16);
        box.setAlignment(Pos.CENTER_LEFT);
        Label playerLabel = new Label(localPlayer != null ? "You: " + localPlayer : "You: PLAYER1");
        playerLabel.getStyleClass().addAll("board-title", "player-label");
        HBox resources = buildResourceBadges(gameState.getCurrentResources());
        Button endTurnButton = new Button("End Turn");
        endTurnButton.getStyleClass().addAll("action-button", "end-turn-btn");
        endTurnButton.setOnAction(e -> endTurn());
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.getChildren().addAll(playerLabel, spacer, resources, endTurnButton);
        return box;
    }

    private VBox buildHandPanel() {
        VBox handPanel = new VBox(10);
        handPanel.setAlignment(Pos.CENTER_LEFT);
        handPanel.getStyleClass().add("hand-panel");
        handRow.getChildren().clear();
        handRow.setAlignment(Pos.CENTER_LEFT);
        handRow.setSpacing(16);
        Label handTitle = new Label("Hand");
        handTitle.getStyleClass().add("subtitle-label");
        for (Card card : hand) {
            Button cardButton = createHandCardButton(card);
            handRow.getChildren().add(cardButton);
        }
        handPanel.getChildren().add(handTitle);
        ScrollPane scroller = new ScrollPane(handRow);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setPannable(true);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.getStyleClass().add("hand-scroller");
        scroller.setPrefViewportHeight(200);
        handPanel.getChildren().addAll(scroller);
        return handPanel;
    }

    private HBox buildStatusStrip() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        statusLabel.getStyleClass().add("status-label");
        box.getChildren().add(statusLabel);
        return box;
    }

    private Button createHandCardButton(Card card) {
        Button cardButton = new Button(card.getName());
        cardButton.getStyleClass().addAll("card-button", "hand-card");
        cardButton.setMinSize(160, 220);
        cardButton.setPrefSize(160, 220);
        cardButton.setUserData(card);
        cardButton.setOnAction(event -> selectCard(card, cardButton));
        UiHelpers.applyHandCardEffect(cardButton, handRow);
        Tooltip tooltip = new Tooltip(card.getName() + "\nCost: " + card.getCost().getEssence() + "E " + card.getCost().getMana() + "M " + card.getCost().getSoul() + "S\nATK:" + card.getAttack() + " HP:" + card.getHealth());
        tooltip.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11px; -fx-background-color: rgba(20,20,20,0.92); -fx-text-fill: white;");
        Tooltip.install(cardButton, tooltip);
        return cardButton;
    }

    private Button createLaneButton(int laneIndex, boolean playerSide, Card occupant) {
        String label = playerSide ? "Play Here" : "Inspect";
        Button button = new Button(label);
        button.getStyleClass().add("lane-action");
        boolean isLocalTurn = isLocalTurn();
        boolean validTarget = selectedCard != null
                && isLocalTurn
                && ((selectedCard.isCreature() && playerSide && occupant == null)
                    || (selectedCard.isSpell() && !playerSide && occupant != null));
        button.setDisable(!validTarget);
        if (validTarget) {
            button.getStyleClass().add("lane-targetable");
        } else {
            button.getStyleClass().remove("lane-targetable");
        }
        button.setOnAction(event -> playSelectedCard(laneIndex));
        return button;
    }

    private void updateHandAvailability(GameState state) {
        ResourcePool resources = state.getCurrentResources();
        for (var node : handRow.getChildren()) {
            if (node instanceof Button cardButton) {
                Card card = (Card) cardButton.getUserData();
                boolean affordable = resources.canAfford(card.getCost());
                cardButton.setDisable(!affordable || state.getCurrentTurn() != localPlayer && gameClient != null);
                cardButton.getStyleClass().remove("unplayable");
                cardButton.getStyleClass().remove("playable");
                if (affordable) {
                    cardButton.getStyleClass().add("playable");
                } else {
                    cardButton.getStyleClass().add("unplayable");
                }
            }
        }
    }

    private void updateLifeLabel(GameState state) {
        lifeLabel.setText("Life — P1: " + state.getPlayer1Life() + " | P2: " + state.getPlayer2Life());
    }

    private void updateTurnBanner(GameState state) {
        turnBanner.setText("Turn: " + state.getCurrentTurn());
        if (state.getCurrentTurn() == Player.PLAYER1) {
            turnBanner.getStyleClass().add("turn-banner-player");
            turnBanner.getStyleClass().remove("turn-banner-opponent");
        } else {
            turnBanner.getStyleClass().add("turn-banner-opponent");
            turnBanner.getStyleClass().remove("turn-banner-player");
        }
        animateTurnBanner();
    }

    private void clearSelection() {
        selectedCard = null;
        selectedCardLabel.setText("No card selected.");
        statusLabel.setText("Select a card, then choose a lane.");
        if (selectedCardButton != null) {
            selectedCardButton.getStyleClass().remove("selected");
            selectedCardButton = null;
        }
    }

    private void renderBoard(GameState state) {
        opponentRow.getChildren().clear();
        playerRow.getChildren().clear();

        if (!isLocalTurn() && selectedCard != null) {
            clearSelection();
        }

        Player me = localPlayer != null ? localPlayer : Player.PLAYER1;
        Player opponent = (me == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;

        List<engine.Lane> oppLanes = state.getLanesForPlayer(opponent);
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            VBox lanePane = buildLanePanel(i, oppLanes.get(i).getOccupant(), false);
            opponentRow.getChildren().add(lanePane);
        }

        List<engine.Lane> myLanes = state.getLanesForPlayer(me);
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            VBox lanePane = buildLanePanel(i, myLanes.get(i).getOccupant(), true);
            playerRow.getChildren().add(lanePane);
        }

        resourceLabel.setText("Resources: " + formatResources(state.getCurrentResources()));
        updateLifeLabel(state);
        updateHandAvailability(state);
        updateTurnBanner(state);
    }

    private VBox buildLanePanel(int laneIndex, Card occupant, boolean playerSide) {
        Label title = new Label("Lane " + (laneIndex + 1));
        title.getStyleClass().add("board-title");
        Label occupantLabel = new Label(occupant == null ? "Empty" : occupant.getName());
        occupantLabel.getStyleClass().add("occupant-label");
        Button actionButton = createLaneButton(laneIndex, playerSide, occupant);
        VBox pane = new VBox(12, title, occupantLabel, actionButton);
        pane.setAlignment(Pos.CENTER);
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.getStyleClass().addAll("lane-panel", "lane");
        HBox.setHgrow(pane, Priority.ALWAYS);
        if (playerSide) {
            pane.setOnMouseClicked(event -> playSelectedCard(laneIndex));
        }
        return pane;
    }

    private Player getLocalTurnOwner() {
        return localPlayer != null ? localPlayer : Player.PLAYER1;
    }

    private boolean isLocalTurn() {
        return gameState.getCurrentTurn() == getLocalTurnOwner();
    }

    private void selectCard(Card card, Button button) {
        if (!isLocalTurn()) {
            statusLabel.setText("Not your turn yet.");
            return;
        }
        if (button.isDisable()) {
            statusLabel.setText("You cannot play that card right now.");
            return;
        }
        this.selectedCard = card;
        selectedCardLabel.setText("Selected card: " + card.getName());
        statusLabel.setText("Choose a lane to play the selected card.");
        if (selectedCardButton != null) {
            selectedCardButton.getStyleClass().remove("selected");
        }
        selectedCardButton = button;
        if (!selectedCardButton.getStyleClass().contains("selected")) {
            selectedCardButton.getStyleClass().add("selected");
        }
        renderBoard(gameState);
    }

    private void playSelectedCard(int laneIndex) {
        if (!isLocalTurn()) {
            statusLabel.setText("Not your turn yet.");
            return;
        }
        if (selectedCard == null) {
            statusLabel.setText("Select a card from your hand first.");
            return;
        }
        try {
            // In networked mode, send the play request to the server and
            // wait for the authoritative StateUpdateMessage. Only mutate
            // the local GameState directly when running standalone.
            if (gameClient != null) {
                gameClient.sendMessage(new PlayCardMessage(selectedCard.getName(), laneIndex));
                statusLabel.setText("Play request sent: " + selectedCard.getName() + " → lane " + (laneIndex + 1));
                clearSelection();
            } else {
                String playedCardName = selectedCard.getName();
                gameState.playCard(selectedCard, laneIndex);
                if (app.getActiveServer() != null) {
                    network.GameStateSnapshot snapshot = network.GameStateSnapshot.fromGameState(gameState);
                    app.getActiveServer().broadcast(new network.StateUpdateMessage(snapshot));
                }
                clearSelection();
                onStateChanged(gameState);
                statusLabel.setText("Played " + playedCardName + " → lane " + (laneIndex + 1));
            }
        } catch (RuntimeException ex) {
            statusLabel.setText(ex.getMessage());
        }
    }

    private void endTurn() {
        if (!isLocalTurn()) {
            statusLabel.setText("Not your turn yet.");
            return;
        }
        // When connected to a server, ask the server to advance the turn
        // and wait for the resulting state update. Locally advance only
        // in standalone mode.
        if (gameClient != null) {
            gameClient.sendMessage(new EndTurnMessage());
            statusLabel.setText("End-turn request sent.");
        } else {
            TurnManager turnManager = app.getActiveServer() != null
                    ? app.getActiveServer().getTurnManager()
                    : new TurnManager(new TimedWinCondition());
            turnManager.nextTurn(gameState);
            if (app.getActiveServer() != null) {
                network.GameStateSnapshot snapshot = network.GameStateSnapshot.fromGameState(gameState);
                app.getActiveServer().broadcast(new network.StateUpdateMessage(snapshot));
            }
            onStateChanged(gameState);
            statusLabel.setText("Turn ended.");
        }
    }

    private String formatResources(ResourcePool pool) {
        return "E:" + pool.getEssence() + " M:" + pool.getMana() + " S:" + pool.getSoul();
    }

    private HBox buildResourceBadges(ResourcePool pool) {
        HBox badges = new HBox(8);
        badges.setAlignment(Pos.CENTER);
        badges.getChildren().addAll(
                createResourceBadge("E", pool.getEssence(), "#9b6b34"),
                createResourceBadge("M", pool.getMana(), "#4b7da4"),
                createResourceBadge("S", pool.getSoul(), "#8c3e5c")
        );
        return badges;
    }

    private HBox createResourceBadge(String label, int value, String color) {
        Label badgeLabel = new Label(label + " " + value);
        badgeLabel.getStyleClass().add("resource-badge");
        badgeLabel.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 12; -fx-padding: 6 10; -fx-text-fill: white;");
        return new HBox(badgeLabel);
    }

    private void animateTurnBanner() {
        // no-op placeholder for future animation integration
    }
}
