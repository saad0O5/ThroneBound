package engine;

/**
 * TimedWinCondition
 * Owner: Member B
 *
 * TODO:
 *   - [ ] Implement checkWin(): true if a Life Total <= 0, OR turn cap (30) reached
 *         (in which case apply the tiebreaker: higher Life Total wins)
 *   - [ ] Implement incrementTurn(), called once per completed turn pair
 */
public class TimedWinCondition implements WinCondition {
    private static final int TURN_CAP = 30;
    private int turnCount;

    public TimedWinCondition() {
        this.turnCount = 0;
    }

    public int getTurnCount() { return turnCount; }

    /** TODO: implement — increments the turn counter. */
    public void incrementTurn() {
        throw new UnsupportedOperationException("TODO: implement incrementTurn()");
    }

    @Override
    public boolean checkWin(GameState state) {
        throw new UnsupportedOperationException("TODO: implement TimedWinCondition");
    }
}
