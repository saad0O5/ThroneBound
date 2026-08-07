package engine;

import cards.Card;

/**
 * Lane represents a single board slot that can hold at most one card.
 */
public class Lane {
    private Card occupant;

    public Lane() {
        this.occupant = null;
    }

    public Card getOccupant() { return occupant; }

    public boolean isEmpty() {
        return occupant == null;
    }

    public void placeCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot place null card in a lane");
        }
        if (!isEmpty()) {
            throw new IllegalStateException("Lane is already occupied");
        }
        this.occupant = card;
    }

    public void removeCard() {
        this.occupant = null;
    }
}
