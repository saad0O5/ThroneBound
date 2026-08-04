package cards;

import engine.GameState;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: "Deck '*--' 25 Card" and "validate(): boolean" —
 * locked-in deck size is exactly 25 cards, no copy-count restriction.
 */
class DeckTest {

    private static class DummyCard extends Card {
        DummyCard(String name) {
            super(name, new Cost(1, 0, 0), 1, 1);
        }

        @Override
        public void play(GameState state) {
            // no-op
        }
    }

    private List<Card> makeCards(int count) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cards.add(new DummyCard("Card " + i));
        }
        return cards;
    }

    @Test
    void deckWithExactlyTwentyFiveCardsIsValid() {
        Deck deck = new Deck(makeCards(25));
        assertTrue(deck.validate());
    }

    @Test
    void deckWithFewerThanTwentyFiveCardsIsInvalid() {
        Deck deck = new Deck(makeCards(20));
        assertFalse(deck.validate());
    }

    @Test
    void deckWithMoreThanTwentyFiveCardsIsInvalid() {
        Deck deck = new Deck(makeCards(30));
        assertFalse(deck.validate());
    }

    @Test
    void deckAllowsDuplicateCardsNoConcopyRestriction() {
        // Locked-in parameter: no copy-count restriction
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            cards.add(new DummyCard("Same Card")); // 25x the same card
        }
        Deck deck = new Deck(cards);
        assertTrue(deck.validate());
    }
}
