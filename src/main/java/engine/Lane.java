package engine;

import cards.Card;

/**
 * Lane
 * Owner: Member B (Game Engine)
 *
 * TODO:
 *   - [ ] Implement isEmpty()
 *   - [ ] Implement placeCard(Card) / removeCard()
 */
public class Lane {
    private Card occupant;

    public Lane() {
        this.occupant = null;
    }

    public Card getOccupant() { return occupant; }

    /** TODO: implement. */
    public boolean isEmpty() {
        throw new UnsupportedOperationException("TODO: implement isEmpty()");
    }

    /** TODO: implement — should reject placing into a non-empty lane. */
    public void placeCard(Card card) {
        throw new UnsupportedOperationException("TODO: implement placeCard()");
    }

    /** TODO: implement — clears the occupant. */
    public void removeCard() {
        throw new UnsupportedOperationException("TODO: implement removeCard()");
    }
}
