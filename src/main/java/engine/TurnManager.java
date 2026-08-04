package engine;

/**
 * TurnManager
 * Owner: Member B (Game Engine)
 *
 * TODO:
 *   - [ ] Implement nextTurn(): switch currentTurn on GameState, accumulate
 *         resources for the newly active player
 *   - [ ] Implement checkWinCondition(): delegate to winCondition.checkWin()
 */
public class TurnManager {
    private final WinCondition winCondition;

    public TurnManager(WinCondition winCondition) {
        this.winCondition = winCondition;
    }

    public WinCondition getWinCondition() { return winCondition; }

    /** TODO: implement. */
    public void nextTurn(GameState state) {
        throw new UnsupportedOperationException("TODO: implement nextTurn()");
    }

    /** TODO: implement. */
    public boolean checkWinCondition(GameState state) {
        throw new UnsupportedOperationException("TODO: implement checkWinCondition()");
    }
}
