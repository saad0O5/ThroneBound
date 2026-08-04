package engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: TurnManager holds a WinCondition and exposes
 * nextTurn() / checkWinCondition().
 */
class TurnManagerTest {

    @Test
    void turnManagerHoldsItsAssignedWinCondition() {
        WinCondition standard = new StandardWinCondition();
        TurnManager manager = new TurnManager(standard);
        assertSame(standard, manager.getWinCondition());
    }

    @Test
    void nextTurnSwitchesActivePlayer() {
        GameState state = new GameState();
        TurnManager manager = new TurnManager(new StandardWinCondition());
        manager.nextTurn(state);
        assertEquals(Player.PLAYER2, state.getCurrentTurn());
    }
}
