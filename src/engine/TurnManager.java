package engine;

/**
 * TurnManager
 * Owner: Member B (Game Engine)
 *
 * Responsibility:
 *   - Advances turns and checks whether the match has ended, using whichever
 *     WinCondition (Standard or Timed) the host selected at match setup.
 *
 * TODO:
 *   - [ ] Field: winCondition (WinCondition) — set from the host's match-setup choice
 *   - [ ] Implement nextTurn(): switch currentTurn, accumulate resources for the
 *         new active player (see ResourcePool)
 *   - [ ] Implement checkWinCondition(GameState): delegate to winCondition.checkWin(),
 *         call after every endTurn()
 *   - [ ] If Timed mode: track turn count and apply the tiebreaker rule (higher
 *         Life Total wins) when the cap is reached
 */
public class TurnManager {
    // TODO: field - WinCondition winCondition

    // TODO: public void checkWinCondition(GameState state)

    // TODO: public void nextTurn()
}
