package gui;

import cards.Card;
import cards.Deck;
import engine.GameState;
import engine.Player;
import engine.ResourcePool;
import engine.StandardWinCondition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

public class MatchScreen extends BorderPane implements GameStateObserver {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;
    private final GameState gameState;
    private final GameClient gameClient;
    private final List<Card> hand;
    private final VBox lanesBox = new VBox(10);
    private final Label selectedCardLabel = new Label("No card selected.");
    private final Label statusLabel = new Label("Select a card, then choose a lane.");
    private Card selectedCard;
    private Player localPlayer = null;

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
        } else if (app.getActiveGameState() != null && this.gameClient == null) {
            this.gameState = app.getActiveGameState();
        } else {
            this.gameState = new GameState();
        }
        this.hand = new ArrayList<>(deck.getCards().subList(0, Math.min(5, deck.getCards().size())));
        if (this.gameClient != null) {
            this.gameClient.setMessageListener(this::handleNetworkMessage);
        }

        setPadding(new Insets(24));
        setTop(buildTopPanel());
        setCenter(lanesBox);
        setBottom(buildBottomPanel());

        onStateChanged(gameState);
    }

    @Override
    public void onStateChanged(GameState state) {
        Platform.runLater(() -> {
            renderBoard(state);
            if (new StandardWinCondition().checkWin(state)) {
                boolean localWon = state.getPlayer1Life() > 0 && state.getPlayer2Life() <= 0;
                app.showResults(profile, localWon);
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
            GameStateSnapshot snapshot = updateMessage.getStatePayload();
            if (snapshot != null) {
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
        statusLabel.getStyleClass().add("status-label");

        VBox panel = new VBox(8, topRow, selectedCardLabel, statusLabel);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(12));
        return panel;
    }

    private HBox buildBottomPanel() {
        VBox handPanel = new VBox(8);
        handPanel.setAlignment(Pos.CENTER_LEFT);
        handPanel.getStyleClass().add("hand-panel");
        Label handLabel = new Label("Hand");
        HBox handButtons = new HBox(10);
        for (Card card : hand) {
            Button cardButton = new Button(card.getName());
            cardButton.getStyleClass().add("card-button");
            cardButton.setOnAction(event -> selectCard(card));
            UiHelpers.applyHoverEffect(cardButton);
            handButtons.getChildren().add(cardButton);
        }
        Button endTurnButton = new Button("End Turn");
        endTurnButton.getStyleClass().add("action-button");
        endTurnButton.setOnAction(event -> endTurn());
        UiHelpers.applyHoverEffect(endTurnButton);
        handPanel.getChildren().addAll(handLabel, handButtons, endTurnButton);
        return new HBox(20, handPanel);
    }

    private void renderBoard(GameState state) {
        lanesBox.getChildren().clear();
        Label info = new Label("Turn: " + state.getCurrentTurn() + " | P1 Life: " + state.getPlayer1Life() + " | P2 Life: " + state.getPlayer2Life());
        lanesBox.getChildren().add(info);
        // Render from the local player's perspective if known; otherwise
        // default to the original P1-as-player layout.
        Player me = localPlayer != null ? localPlayer : Player.PLAYER1;
        Player opponent = (me == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;

        HBox opponentRow = new HBox(12);
        List<engine.Lane> oppLanes = state.getLanesForPlayer(opponent);
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            opponentRow.getChildren().add(buildLanePanel(i, oppLanes.get(i).getOccupant(), false));
        }

        HBox playerRow = new HBox(12);
        List<engine.Lane> myLanes = state.getLanesForPlayer(me);
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            playerRow.getChildren().add(buildLanePanel(i, myLanes.get(i).getOccupant(), true));
        }

        lanesBox.getChildren().addAll(opponentRow, playerRow);
        Label resourceLabel = new Label("Resources P1: " + formatResources(state.getResourcesP1()) + " | Resources P2: " + formatResources(state.getResourcesP2()));
        lanesBox.getChildren().add(resourceLabel);
    }

    private VBox buildLanePanel(int laneIndex, Card occupant, boolean playerSide) {
        Label title = new Label("Lane " + (laneIndex + 1));
        title.getStyleClass().add("board-title");
        Label occupantLabel = new Label(occupant == null ? "Empty" : occupant.getName());
        Button actionButton = new Button(playerSide ? "Play Here" : "Watch");
        actionButton.setOnAction(event -> playSelectedCard(laneIndex));
        VBox pane = new VBox(6, title, occupantLabel, actionButton);
        pane.getStyleClass().add("lane-panel");
        return pane;
    }

    private void selectCard(Card card) {
        this.selectedCard = card;
        selectedCardLabel.setText("Selected card: " + card.getName());
        statusLabel.setText("Choose a lane to play the selected card.");
    }

    private void playSelectedCard(int laneIndex) {
        if (selectedCard == null) {
            statusLabel.setText("Select a card from your hand first.");
            return;
        }
        if (gameClient != null && localPlayer != null && gameState.getCurrentTurn() != localPlayer) {
            statusLabel.setText("Not your turn yet.");
            return;
        }
        try {
            // In networked mode, send the play request to the server and
            // wait for the authoritative StateUpdateMessage. Only mutate
            // the local GameState directly when running standalone.
            if (gameClient != null) {
                gameClient.sendMessage(new PlayCardMessage(selectedCard.getName(), laneIndex));
                statusLabel.setText("Play request sent: " + selectedCard.getName() + " -> lane " + (laneIndex + 1));
            } else {
                // Local host: apply to the authoritative server GameState and
                // broadcast the update to connected clients so they see the play.
                gameState.playCard(selectedCard, laneIndex);
                if (app.getActiveServer() != null) {
                    network.GameStateSnapshot snapshot = network.GameStateSnapshot.fromGameState(gameState);
                    app.getActiveServer().broadcast(new network.StateUpdateMessage(snapshot));
                }
                onStateChanged(gameState);
                statusLabel.setText("Played " + selectedCard.getName() + " to lane " + (laneIndex + 1));
            }
        } catch (RuntimeException ex) {
            statusLabel.setText(ex.getMessage());
        }
    }

    private void endTurn() {
        if (gameClient != null && localPlayer != null && gameState.getCurrentTurn() != localPlayer) {
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
            gameState.endTurn();
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
}
