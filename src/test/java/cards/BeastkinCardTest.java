package cards;

import engine.GameState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: BeastkinCard extends Card and adds Pack-synergy
 * behavior (countOtherBeastkinInPlay). Uses a local test subclass since no
 * concrete Beastkin cards (e.g. Wolf Pup) exist yet.
 *
 * When you implement a real card like Wolf Pup, add a WolfPupTest following
 * this same pattern instead of (or alongside) this generic one.
 */
class BeastkinCardTest {

    private static class MinimalBeastkinCard extends BeastkinCard {
        MinimalBeastkinCard(String name, Cost cost, int attack, int health) {
            super(name, cost, attack, health);
        }

        @Override
        public void play(GameState state) {
            // no-op for this test fixture
        }
    }

    @Test
    void beastkinCardIsACard() {
        BeastkinCard card = new MinimalBeastkinCard("Test Beastkin", new Cost(1, 0, 0), 1, 1);
        assertInstanceOf(Card.class, card);
    }

    @Test
    void countOtherBeastkinInPlayIsCallableOnceImplemented() {
        // RED until countOtherBeastkinInPlay() is implemented
        MinimalBeastkinCard card = new MinimalBeastkinCard("Test Beastkin", new Cost(1, 0, 0), 1, 1);
        GameState state = new GameState();
        assertDoesNotThrow(() -> card.countOtherBeastkinInPlay(state));
    }
}
