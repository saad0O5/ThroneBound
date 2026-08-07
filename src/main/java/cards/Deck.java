package cards;

import java.util.List;

/**
 * Deck
 * Owner: Member B (also used by Member C's DeckBuilderScreen)
 */
public class Deck {
    private final List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() { return cards; }

    /** Validate per the 25-card deck-size rule. */
    public boolean validate() {
        return cards.size() == 25;
    }
}
