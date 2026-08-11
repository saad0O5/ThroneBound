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
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import persistence.PlayerProfile;
import persistence.ProfileManager;

import java.util.ArrayList;
import java.util.List;

public class DeckBuilderScreen extends BorderPane {
    private final ThroneBoundApp app;
    private final PlayerProfile profile;
    private final List<String> selectedCards = new ArrayList<>();
    private final FlowPane cardGrid = new FlowPane(14, 14);
    private final VBox selectedCardsPane = new VBox(8);
    private final Label deckStatusLabel = new Label("Selected 0 / 25 cards");
    private final ProgressBar selectionProgressBar = new ProgressBar(0);
    private final Button readyButton = new Button("Ready");
    private Faction selectedFaction;

    public DeckBuilderScreen(ThroneBoundApp app, PlayerProfile profile) {
        this.app = app;
        this.profile = profile;

        setPadding(new Insets(24));
        getStyleClass().add("screen-root");

        VBox sidePanel = new VBox(12);
        sidePanel.setAlignment(Pos.TOP_LEFT);
        Label title = new Label("Build a 25-card Deck");
        title.getStyleClass().add("title-label");

        Label subtitle = new Label("Select cards from your unlocked pool");
        subtitle.getStyleClass().add("subtitle-label");

        Button beastkinButton = new Button("Beastkin Clans");
        Button arcaneButton = new Button("Arcane Order");
        Button undeadButton = new Button("Undead Legion");
        Button backButton = new Button("Back");

        beastkinButton.setOnAction(event -> selectFaction(new BeastkinFaction()));
        arcaneButton.setOnAction(event -> selectFaction(new ArcaneOrderFaction()));
        undeadButton.setOnAction(event -> selectFaction(new UndeadLegionFaction()));
        backButton.setOnAction(event -> app.showMainMenu(profile));
        readyButton.setText("Start Match");
        readyButton.setOnAction(event -> finishDeck());
        readyButton.setDisable(true);
        backButton.getStyleClass().add("secondary-button");
        readyButton.getStyleClass().add("action-button");

        sidePanel.getStyleClass().add("panel");
        sidePanel.getChildren().addAll(title, subtitle, beastkinButton, arcaneButton, undeadButton, backButton, readyButton, deckStatusLabel, selectionProgressBar, selectedCardsPane);

        cardGrid.getStyleClass().add("card-grid");
        cardGrid.setMinWidth(640);

        setLeft(sidePanel);
        setCenter(cardGrid);

        selectFaction(new BeastkinFaction());
    }

    private void selectFaction(Faction faction) {
        this.selectedFaction = faction;
        cardGrid.getChildren().clear();
        selectedCardsPane.getChildren().clear();
        selectedCardsPane.getChildren().add(new Label("Selected cards:"));
        updateSelectionPreview();

        if (availableCardsForFaction(faction).isEmpty()) {
            Label emptyLabel = new Label("No unlocked cards for this faction yet.");
            emptyLabel.getStyleClass().add("info-label");
            cardGrid.getChildren().add(emptyLabel);
            return;
        }
        for (Card card : availableCardsForFaction(faction)) {
            Button cardButton = new Button(card.getName() + "\n" + formatCost(card.getCost()));
            cardButton.getStyleClass().add("card-button");
            cardButton.setMinSize(180, 180);
            cardButton.setWrapText(true);
            UiHelpers.applyHoverEffect(cardButton);
            cardButton.setOnAction(event -> addCard(card.getName()));
            cardGrid.getChildren().add(cardButton);
        }
    }

    private void addCard(String cardName) {
        if (selectedCards.size() >= 25) {
            deckStatusLabel.setText("Deck is already full.");
            return;
        }
        selectedCards.add(cardName);
        deckStatusLabel.setText("Selected " + selectedCards.size() + " / 25 cards");
        selectionProgressBar.setProgress(selectedCards.size() / 25.0);
        readyButton.setDisable(selectedCards.size() != 25);
        updateSelectionPreview();
    }

    private void updateSelectionPreview() {
        selectedCardsPane.getChildren().clear();
        Label previewTitle = new Label("Selected cards:");
        previewTitle.getStyleClass().add("subtitle-label");
        selectedCardsPane.getChildren().add(previewTitle);
        int displayCount = Math.min(selectedCards.size(), 6);
        for (int i = 0; i < displayCount; i++) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            Label cardLabel = new Label((i + 1) + ". " + selectedCards.get(i));
            cardLabel.getStyleClass().add("info-label");
            Button removeButton = new Button("Remove");
            removeButton.getStyleClass().add("secondary-button");
            int finalI = i;
            removeButton.setOnAction(event -> removeCard(finalI));
            UiHelpers.applyHoverEffect(removeButton);
            row.getChildren().addAll(cardLabel, removeButton);
            selectedCardsPane.getChildren().add(row);
        }
        if (selectedCards.size() > 6) {
            selectedCardsPane.getChildren().add(new Label("..." + (selectedCards.size() - 6) + " more"));
        }
    }

    private void removeCard(int index) {
        if (index >= 0 && index < selectedCards.size()) {
            selectedCards.remove(index);
            deckStatusLabel.setText("Selected " + selectedCards.size() + " / 25 cards");
            selectionProgressBar.setProgress(selectedCards.size() / 25.0);
            readyButton.setDisable(selectedCards.size() != 25);
            updateSelectionPreview();
        }
    }

    private void finishDeck() {
        if (selectedCards.size() != 25) {
            deckStatusLabel.setText("A valid deck must contain exactly 25 cards.");
            return;
        }

        Deck deck = new Deck(resolveCards(selectedCards));
        profile.saveDeck(deck);
        new persistence.ProfileManager("profiles").save(profile);
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
