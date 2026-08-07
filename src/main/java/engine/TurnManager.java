package engine;

/**
 * TurnManager advances turn order and delegates win checks to the active win condition.
 */
public class TurnManager {
    private final WinCondition winCondition;

    public TurnManager(WinCondition winCondition) {
        this.winCondition = winCondition;
    }

    public WinCondition getWinCondition() { return winCondition; }

    public void nextTurn(GameState state) {
        state.endTurn();
        if (winCondition instanceof TimedWinCondition timedWinCondition) {
            timedWinCondition.incrementTurn();
        }
    }

    public boolean checkWinCondition(GameState state) {
        return winCondition.checkWin(state);
    }
}
