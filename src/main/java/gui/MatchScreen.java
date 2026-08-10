package gui;

import cards.Card;
import cards.Deck;
import engine.GameState;
import engine.Player;
import engine.ResourcePool;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.EndTurnMessage;
import network.GameClient;
import network.PlayCardMessage;
import persistence.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

public class MatchScreen extends BorderPane implements GameStateObserver {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;
    private final GameState gameState;
    private final GameClient gameClient;
    private final List<Card> hand;
    private final VBox lanesBox = new VBox(8);
    private final Label statusLabel = new Label();
    private Card selectedCard;

    public MatchScreen(ThroneBoundApp app, PlayerProfile profile, Deck deck) {
        this.app = app;
        this.profile = profile;
        this.gameState = app.getActiveGameState() != null ? app.getActiveGameState() : new GameState();
        this.gameClient = app.getActiveClient();
        this.hand = new ArrayList<>(deck.getCards().subList(0, Math.min(5, deck.getCards().size())));
        app.setActiveGameState(this.gameState);

        setPadding(new Insets(24));
        setTop(buildTopPanel());
        setCenter(lanesBox);
        setBottom(buildBottomPanel());

        onStateChanged(gameState);
    }

    @Override
    public void onStateChanged(GameState state) {
        Platform.runLater(() -> renderBoard(state));
    }

    private VBox buildTopPanel() {
        Label title = new Label("Match in Progress");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox topRow = new HBox(20, title, statusLabel);
        topRow.setAlignment(Pos.CENTER_LEFT);
        VBox panel = new VBox(8, topRow);
        panel.setPadding(new Insets(0, 0, 12, 0));
        return panel;
    }

    private HBox buildBottomPanel() {
        VBox handPanel = new VBox(8);
        handPanel.setAlignment(Pos.CENTER_LEFT);
        Label handLabel = new Label("Hand");
        HBox handButtons = new HBox(8);
        for (Card card : hand) {
            Button cardButton = new Button(card.getName());
            cardButton.setOnAction(event -> selectCard(card));
            handButtons.getChildren().add(cardButton);
        }
        Button endTurnButton = new Button("End Turn");
        endTurnButton.setOnAction(event -> endTurn());
        handPanel.getChildren().addAll(handLabel, handButtons, endTurnButton);
        return new HBox(20, handPanel);
    }

    private void renderBoard(GameState state) {
        lanesBox.getChildren().clear();
        Label info = new Label("Turn: " + state.getCurrentTurn() + " | P1 Life: " + state.getPlayer1Life() + " | P2 Life: " + state.getPlayer2Life());
        lanesBox.getChildren().add(info);

        HBox opponentRow = new HBox(12);
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            opponentRow.getChildren().add(buildLanePanel(i, state.getLanesP2().get(i).getOccupant(), false));
        }

        HBox playerRow = new HBox(12);
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            playerRow.getChildren().add(buildLanePanel(i, state.getLanesP1().get(i).getOccupant(), true));
        }

        lanesBox.getChildren().addAll(opponentRow, playerRow);
        Label resourceLabel = new Label("Resources P1: " + formatResources(state.getResourcesP1()) + " | Resources P2: " + formatResources(state.getResourcesP2()));
        lanesBox.getChildren().add(resourceLabel);
    }

    private VBox buildLanePanel(int laneIndex, Card occupant, boolean playerSide) {
        Label title = new Label("Lane " + (laneIndex + 1));
        Label occupantLabel = new Label(occupant == null ? "Empty" : occupant.getName());
        Button actionButton = new Button(playerSide ? "Play Here" : "Watch");
        actionButton.setOnAction(event -> playSelectedCard(laneIndex));
        VBox pane = new VBox(6, title, occupantLabel, actionButton);
        pane.setPadding(new Insets(8));
        pane.setStyle("-fx-border-color: lightgray; -fx-border-radius: 4; -fx-padding: 8;");
        return pane;
    }

    private void selectCard(Card card) {
        this.selectedCard = card;
        statusLabel.setText("Selected " + card.getName());
    }

    private void playSelectedCard(int laneIndex) {
        if (selectedCard == null) {
            statusLabel.setText("Select a card from your hand first.");
            return;
        }
        try {
            gameState.playCard(selectedCard, laneIndex);
            if (gameClient != null) {
                gameClient.sendMessage(new PlayCardMessage(selectedCard.getName(), laneIndex));
            }
            onStateChanged(gameState);
            statusLabel.setText("Played " + selectedCard.getName() + " to lane " + (laneIndex + 1));
        } catch (RuntimeException ex) {
            statusLabel.setText(ex.getMessage());
        }
    }

    private void endTurn() {
        gameState.endTurn();
        if (gameClient != null) {
            gameClient.sendMessage(new EndTurnMessage());
        }
        onStateChanged(gameState);
        statusLabel.setText("Turn ended.");
    }

    private String formatResources(ResourcePool pool) {
        return "E:" + pool.getEssence() + " M:" + pool.getMana() + " S:" + pool.getSoul();
    }
}
