package persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerProfile
 * Owner: Member C (GUI & Persistence)
 *
 * TODO:
 *   - [ ] Nothing further required for the basic fields below — extend with
 *         savedDecks / matchHistory references as you build DeckBuilderScreen
 *         and ResultsScreen
 */
public class PlayerProfile {
    private final String username;
    private final String passwordHash;
    private final List<String> unlockedCards;

    public PlayerProfile(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.unlockedCards = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public List<String> getUnlockedCards() { return unlockedCards; }

    public void addUnlockedCard(String cardName) {
        unlockedCards.add(cardName);
    }
}
