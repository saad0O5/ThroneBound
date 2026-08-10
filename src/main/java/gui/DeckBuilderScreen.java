package gui;

import cards.Card;
import cards.Cost;
import cards.Faction;
import cards.ArcaneOrderFaction;
import cards.BeastkinFaction;
import cards.Deck;
import cards.UndeadLegionFaction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import persistence.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

public class DeckBuilderScreen extends BorderPane {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;
    private final List<String> selectedCards = new ArrayList<>();
    private final VBox cardListBox = new VBox(8);
    private final Label deckStatusLabel = new Label("Selected 0 / 25 cards");
    private final Button readyButton = new Button("Ready");
    private Faction selectedFaction;

    public DeckBuilderScreen(ThroneBoundApp app, PlayerProfile profile) {
        this.app = app;
        this.profile = profile;

        setPadding(new Insets(24));

        VBox sidePanel = new VBox(10);
        sidePanel.setAlignment(Pos.TOP_LEFT);
        Label title = new Label("Build a 25-card Deck");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button beastkinButton = new Button("Beastkin Clans");
        Button arcaneButton = new Button("Arcane Order");
        Button undeadButton = new Button("Undead Legion");
        Button backButton = new Button("Back");

        beastkinButton.setOnAction(event -> selectFaction(new BeastkinFaction()));
        arcaneButton.setOnAction(event -> selectFaction(new ArcaneOrderFaction()));
        undeadButton.setOnAction(event -> selectFaction(new UndeadLegionFaction()));
        backButton.setOnAction(event -> app.showMainMenu(profile));
        readyButton.setOnAction(event -> finishDeck());
        readyButton.setDisable(true);

        sidePanel.getChildren().addAll(title, beastkinButton, arcaneButton, undeadButton, backButton, readyButton, deckStatusLabel);

        setLeft(sidePanel);
        setCenter(cardListBox);

        selectFaction(new BeastkinFaction());
    }

    private void selectFaction(Faction faction) {
        this.selectedFaction = faction;
        cardListBox.getChildren().clear();
        for (Card card : availableCardsForFaction(faction)) {
            Button cardButton = new Button(card.getName() + "  " + formatCost(card.getCost()));
            cardButton.setOnAction(event -> addCard(card.getName()));
            cardListBox.getChildren().add(cardButton);
        }
    }

    private void addCard(String cardName) {
        if (selectedCards.size() >= 25) {
            deckStatusLabel.setText("Deck is already full.");
            return;
        }
        selectedCards.add(cardName);
        deckStatusLabel.setText("Selected " + selectedCards.size() + " / 25 cards");
        readyButton.setDisable(selectedCards.size() != 25);
    }

    private void finishDeck() {
        if (selectedCards.size() != 25) {
            deckStatusLabel.setText("A valid deck must contain exactly 25 cards.");
            return;
        }

        Deck deck = new Deck(resolveCards(selectedCards));
        profile.saveDeck(deck);
        app.showMatch(profile, deck);
    }

    private List<Card> availableCardsForFaction(Faction faction) {
        List<Card> unlocked = new ArrayList<>();
        List<String> unlockedNames = profile.getUnlockedCards();
        boolean hasUnlockedCards = !unlockedNames.isEmpty();
        for (Card card : faction.getCardPool()) {
            if (!hasUnlockedCards || unlockedNames.contains(card.getName())) {
                unlocked.add(card);
            }
        }
        return unlocked;
    }

    private List<Card> resolveCards(List<String> cardNames) {
        List<Card> cards = new ArrayList<>();
        List<Faction> factions = List.of(new BeastkinFaction(), new ArcaneOrderFaction(), new UndeadLegionFaction());
        for (String name : cardNames) {
            for (Faction faction : factions) {
                for (Card card : faction.getCardPool()) {
                    if (card.getName().equals(name)) {
                        cards.add(card);
                        break;
                    }
                }
            }
        }
        return cards;
    }

    private String formatCost(Cost cost) {
        return "[E:" + cost.getEssence() + " M:" + cost.getMana() + " S:" + cost.getSoul() + "]";
    }
}
