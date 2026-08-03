package engine;

/**
 * WinCondition (interface)
 * Owner: Member B (Game Engine)
 *
 * Responsibility:
 *   - Abstraction over how a match ends. Two implementations exist per the
 *     locked-in project parameters: StandardWinCondition and TimedWinCondition.
 *   - The host picks one at match setup (see gui.HostJoinScreen).
 *
 * TODO:
 *   - [ ] Declare checkWin(GameState state): boolean — returns true when the
 *         match should end (used by TurnManager.checkWinCondition())
 */
public interface WinCondition {
    // TODO: boolean checkWin(GameState state);
}
