package engine;

import cards.Card;
import cards.Cost;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: Lane has "occupant: Card" and "isEmpty(): boolean".
 */
class LaneTest {

    @Test
    void newLaneIsEmpty() {
        Lane lane = new Lane();
        assertTrue(lane.isEmpty());
    }

    @Test
    void placingACardMakesLaneNotEmpty() {
        Lane lane = new Lane();
        Card card = new engine.TestCards.SimpleCard("Wolf Pup", new Cost(1, 0, 0), 1, 1);
        lane.placeCard(card);
        assertFalse(lane.isEmpty());
        assertEquals(card, lane.getOccupant());
    }

    @Test
    void removingCardMakesLaneEmptyAgain() {
        Lane lane = new Lane();
        Card card = new engine.TestCards.SimpleCard("Wolf Pup", new Cost(1, 0, 0), 1, 1);
        lane.placeCard(card);
        lane.removeCard();
        assertTrue(lane.isEmpty());
        assertNull(lane.getOccupant());
    }
}
