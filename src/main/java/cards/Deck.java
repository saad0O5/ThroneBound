package cards;

import java.util.List;

/**
 * Deck
 * Owner: Member B (also used by Member C's DeckBuilderScreen)
 *
 * TODO:
 *   - [ ] Implement validate(): true only if cards.size() == 25 (locked-in deck
 *         size — no copy-count restriction, per project parameters)
 */
public class Deck {
    private final List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() { return cards; }

    /** TODO: implement per the 25-card deck-size rule. */
    public boolean validate() {
        throw new UnsupportedOperationException("TODO: implement deck validation");
    }
}
