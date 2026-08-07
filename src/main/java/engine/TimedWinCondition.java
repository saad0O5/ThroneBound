package engine;

/**
 * TimedWinCondition ends the match when a player reaches 0 life or when the
 * turn cap is reached and one player has a greater life total.
 */
public class TimedWinCondition implements WinCondition {
    private static final int TURN_CAP = 30;
    private int turnCount;

    public TimedWinCondition() {
        this.turnCount = 0;
    }

    public int getTurnCount() { return turnCount; }

    public void incrementTurn() {
        turnCount++;
    }

    @Override
    public boolean checkWin(GameState state) {
        if (state.getPlayer1Life() <= 0 || state.getPlayer2Life() <= 0) {
            return true;
        }
        return turnCount >= TURN_CAP;
    }
}
