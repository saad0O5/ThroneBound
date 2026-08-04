package cards;

import engine.GameState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: Card is abstract with name/cost/attack/health
 * fields and a play(GameState) method; onDeath(GameState) defaults to a no-op.
 *
 * Uses a local test-only subclass since no concrete cards exist yet — this
 * verifies the base Card contract in isolation from any specific faction.
 */
class CardTest {

    private static class MinimalCard extends Card {
        MinimalCard(String name, Cost cost, int attack, int health) {
            super(name, cost, attack, health);
        }

        @Override
        public void play(GameState state) {
            // no-op for this test fixture
        }
    }

    @Test
    void cardExposesItsConstructedStats() {
        Cost cost = new Cost(2, 1, 0);
        Card card = new MinimalCard("Test Card", cost, 3, 4);

        assertEquals("Test Card", card.getName());
        assertEquals(cost, card.getCost());
        assertEquals(3, card.getAttack());
        assertEquals(4, card.getHealth());
    }

    @Test
    void setHealthUpdatesHealth() {
        Card card = new MinimalCard("Test Card", new Cost(1, 0, 0), 1, 5);
        card.setHealth(2);
        assertEquals(2, card.getHealth());
    }

    @Test
    void onDeathDefaultsToNoOp() {
        Card card = new MinimalCard("Test Card", new Cost(1, 0, 0), 1, 1);
        // Should not throw — base Card.onDeath() is a documented no-op
        assertDoesNotThrow(() -> card.onDeath(new GameState()));
    }
}
