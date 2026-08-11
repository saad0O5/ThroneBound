package cards;

import java.util.ArrayList;
import java.util.List;

import cards.CardFactory;

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

    public List<String> getCardNames() {
        List<String> names = new ArrayList<>();
        for (Card card : cards) {
            names.add(card.getName());
        }
        return names;
    }

    public static Deck fromCardNames(List<String> cardNames) {
        List<Card> cards = new ArrayList<>();
        for (String name : cardNames) {
            cards.add(CardFactory.createCard(name));
        }
        return new Deck(cards);
    }

    /** Validate per the 12-card deck-size rule. */
    public boolean validate() {
        return cards.size() == 12;
    }
}
