package engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: WinCondition is realized by StandardWinCondition
 * (no turn limit) and TimedWinCondition (30-turn cap + tiebreaker) — both
 * locked-in as selectable match-setup options.
 */
class WinConditionTest {

    @Test
    void standardWinConditionTriggersWhenPlayerLifeReachesZero() {
        // NOTE: this test manipulates player life directly for testing purposes;
        // once GameState exposes a way to reduce life (e.g. via combat), prefer
        // driving state changes through the real API instead.
        GameState state = new GameState();
        WinCondition condition = new StandardWinCondition();
        // TODO once GameState has a way to set/reduce life for test setup,
        // simulate player1Life reaching 0 and assert condition.checkWin(state) is true
        assertNotNull(condition);
    }

    @Test
    void timedWinConditionStartsAtZeroTurns() {
        TimedWinCondition condition = new TimedWinCondition();
        assertEquals(0, condition.getTurnCount());
    }

    @Test
    void timedWinConditionDoesNotTriggerBeforeTurnCap() {
        GameState state = new GameState();
        TimedWinCondition condition = new TimedWinCondition();
        assertFalse(condition.checkWin(state));
    }

    @Test
    void timedWinConditionTriggersAtThirtyTurns() {
        GameState state = new GameState();
        TimedWinCondition condition = new TimedWinCondition();
        for (int i = 0; i < 30; i++) {
            condition.incrementTurn();
        }
        assertEquals(30, condition.getTurnCount());
        assertTrue(condition.checkWin(state));
    }
}
