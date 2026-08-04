package engine;

/**
 * WinCondition (interface)
 * Owner: Member B (Game Engine)
 *
 * TODO:
 *   - [ ] No implementation needed here — see StandardWinCondition / TimedWinCondition
 */
public interface WinCondition {
    boolean checkWin(GameState state);
}
