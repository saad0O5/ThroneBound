package engine;

/**
 * TimedWinCondition
 * Owner: Member B (Game Engine)
 * Implements: WinCondition
 *
 * Responsibility:
 *   - 30-turn cap; if neither player's Life Total has hit 0 by then, the player
 *     with the higher Life Total wins (tiebreaker rule).
 *
 * TODO:
 *   - [ ] Track turn count (likely needs a reference back to TurnManager, or its
 *         own counter incremented externally)
 *   - [ ] Implement checkWin(GameState state): true if a Life Total <= 0 OR
 *         turn count has reached 30 (apply tiebreaker in the latter case)
 */
public class TimedWinCondition /* implements WinCondition */ {
    // TODO
}
