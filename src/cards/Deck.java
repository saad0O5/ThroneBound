package cards;

/**
 * Deck
 * Owner: Member B (also used by Member C's DeckBuilderScreen)
 *
 * Responsibility:
 *   - Represents a player's 25-card deck for a match, built from their chosen
 *     faction's unlocked cards (+ neutral pool). No copy-count restriction
 *     per locked-in project parameters.
 *
 * TODO:
 *   - [ ] Field: cards (List<Card>, exactly 25 when valid)
 *   - [ ] Implement validate(): checks deck size == 25 and all cards belong to
 *         the chosen faction or the neutral pool
 *   - [ ] Add save/load hooks so persistence.PlayerProfile can store a player's
 *         saved deck configurations as JSON
 */
public class Deck {
    // TODO: field - List<Card> cards

    // TODO: public boolean validate()
}
