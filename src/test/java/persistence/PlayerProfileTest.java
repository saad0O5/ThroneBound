package persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: PlayerProfile holds username, passwordHash,
 * and unlockedCards.
 */
class PlayerProfileTest {

    @Test
    void profileExposesUsernameAndPasswordHash() {
        PlayerProfile profile = new PlayerProfile("hero123", "hashedpw");
        assertEquals("hero123", profile.getUsername());
        assertEquals("hashedpw", profile.getPasswordHash());
    }

    @Test
    void newProfileHasNoUnlockedCards() {
        PlayerProfile profile = new PlayerProfile("hero123", "hashedpw");
        assertTrue(profile.getUnlockedCards().isEmpty());
    }

    @Test
    void addUnlockedCardAddsToList() {
        PlayerProfile profile = new PlayerProfile("hero123", "hashedpw");
        profile.addUnlockedCard("Alpha Wolf");
        assertEquals(1, profile.getUnlockedCards().size());
        assertTrue(profile.getUnlockedCards().contains("Alpha Wolf"));
    }
}
