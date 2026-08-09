package persistence;

import java.util.ArrayList;
import java.util.List;

public class PlayerProfile {
    private final String username;
    private final String passwordHash;
    private final List<String> unlockedCards;
    private final MatchHistory matchHistory;

    public PlayerProfile(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.unlockedCards = new ArrayList<>();
        this.matchHistory = new MatchHistory();
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public List<String> getUnlockedCards() { return unlockedCards; }
    public MatchHistory getMatchHistory() { return matchHistory; }

    public void addUnlockedCard(String cardName) {
        unlockedCards.add(cardName);
    }
}
