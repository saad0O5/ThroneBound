package persistence;

import cards.Deck;

import java.util.ArrayList;
import java.util.List;

public class PlayerProfile {
    private final String username;
    private final String passwordHash;
    private final List<String> unlockedCards;
    private final MatchHistory matchHistory;
    private final List<Deck> savedDecks;

    public PlayerProfile(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.unlockedCards = new ArrayList<>();
        this.matchHistory = new MatchHistory();
        this.savedDecks = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public List<String> getUnlockedCards() { return unlockedCards; }
    public MatchHistory getMatchHistory() { return matchHistory; }
    public List<Deck> getSavedDecks() { return savedDecks; }

    public void addUnlockedCard(String cardName) {
        unlockedCards.add(cardName);
    }

    public void saveDeck(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        savedDecks.add(deck);
    }
}
