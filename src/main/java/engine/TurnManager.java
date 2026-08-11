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

    /**
     * Determine the match result when a win condition is met.
     * Returns `ONGOING` if `winCondition.checkWin(state)` is false.
     */
    public MatchResult determineWinner(GameState state) {
        if (!winCondition.checkWin(state)) {
            return MatchResult.ONGOING;
        }

        int p1Life = state.getPlayer1Life();
        int p2Life = state.getPlayer2Life();

        // Direct lethal conditions
        boolean p1Dead = p1Life <= 0;
        boolean p2Dead = p2Life <= 0;
        if (p1Dead && p2Dead) return MatchResult.DRAW;
        if (p1Dead) return MatchResult.PLAYER2;
        if (p2Dead) return MatchResult.PLAYER1;

        // If timed cap triggered, compare life totals (higher life wins)
        if (winCondition instanceof TimedWinCondition timed && timed.isAtCap()) {
            if (p1Life > p2Life) return MatchResult.PLAYER1;
            if (p2Life > p1Life) return MatchResult.PLAYER2;
            return MatchResult.DRAW;
        }

        // Fallback: no clear winner, treat as ongoing
        return MatchResult.ONGOING;
    }
}
