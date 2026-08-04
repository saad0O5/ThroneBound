package engine;

import cards.Card;
import cards.Cost;

/**
 * Test-only fixture. NOT part of the production card set — provides a
 * minimal concrete Card implementation so engine tests can construct and
 * manipulate Card objects without depending on real faction cards (which
 * don't exist yet). Delete/replace usages once real cards from
 * docs/Card_List.md are implemented, if you'd rather test engine logic
 * against real cards.
 */
class TestCards {
    static class SimpleCard extends Card {
        SimpleCard(String name, Cost cost, int attack, int health) {
            super(name, cost, attack, health);
        }

        @Override
        public void play(GameState state) {
            // no-op — this fixture card has no special effect
        }
    }
}
