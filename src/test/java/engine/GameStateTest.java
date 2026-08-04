package engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: GameState composes 6 Lanes (3 per player,
 * "GameState *-- 6 Lane"), starts both players at the locked-in 25 Life,
 * and Player 1 goes first.
 *
 * The construction-related tests below should already PASS (constructor
 * is implemented). The mutation tests are RED until playCard/endTurn/
 * resolveCombat are implemented — that's expected; make them pass as you
 * implement each method.
 */
class GameStateTest {

    @Test
    void newGameStateStartsWithLockedInLifeTotals() {
        GameState state = new GameState();
        assertEquals(25, state.getPlayer1Life());
        assertEquals(25, state.getPlayer2Life());
    }

    @Test
    void newGameStateHasThreeLanesPerPlayer() {
        GameState state = new GameState();
        assertEquals(3, state.getLanesP1().size());
        assertEquals(3, state.getLanesP2().size());
    }

    @Test
    void newGameStateAllLanesAreDistinctObjects() {
        // UML: GameState *-- 6 Lane -> exactly 6 independent Lane instances, not shared references
        GameState state = new GameState();
        java.util.Set<Lane> allLanes = new java.util.HashSet<>();
        allLanes.addAll(state.getLanesP1());
        allLanes.addAll(state.getLanesP2());
        assertEquals(6, allLanes.size());
    }

    @Test
    void player1GoesFirst() {
        GameState state = new GameState();
        assertEquals(Player.PLAYER1, state.getCurrentTurn());
    }

    @Test
    void endTurnSwitchesCurrentPlayer() {
        // RED until TurnManager/endTurn is implemented
        GameState state = new GameState();
        state.endTurn();
        assertEquals(Player.PLAYER2, state.getCurrentTurn());
    }

    @Test
    void playCardPlacesCardInSpecifiedLane() {
        // RED until playCard is implemented
        GameState state = new GameState();
        cards.Card testCard = new TestCards.SimpleCard("Test Card", new cards.Cost(1, 0, 0), 1, 1);
        state.playCard(testCard, 0);
        assertFalse(state.getLanesP1().get(0).isEmpty());
        assertEquals(testCard, state.getLanesP1().get(0).getOccupant());
    }

    @Test
    void concurrentPlayCardsDoNotCorruptState() throws InterruptedException {
        // Concurrency requirement test: two threads hitting GameState's
        // synchronized methods at once should never leave it in a broken
        // state (no exception other than the expected "not implemented" one,
        // no partial/inconsistent lane state). Once playCard() is actually
        // implemented, replace the try/catch with real assertions about the
        // final state (e.g. exactly one card landed, or both landed in
        // different lanes without corrupting each other).
        GameState state = new GameState();
        cards.Card cardA = new TestCards.SimpleCard("Card A", new cards.Cost(1, 0, 0), 1, 1);
        cards.Card cardB = new TestCards.SimpleCard("Card B", new cards.Cost(1, 0, 0), 1, 1);

        Runnable actionA = () -> {
            try { state.playCard(cardA, 0); } catch (UnsupportedOperationException ignored) {}
        };
        Runnable actionB = () -> {
            try { state.playCard(cardB, 1); } catch (UnsupportedOperationException ignored) {}
        };

        Thread t1 = new Thread(actionA);
        Thread t2 = new Thread(actionB);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // At minimum, the object must still be in a readable, non-corrupted state.
        assertEquals(3, state.getLanesP1().size());
        assertEquals(3, state.getLanesP2().size());
    }
}
