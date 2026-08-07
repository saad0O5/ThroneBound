package engine;

/**
 * StandardWinCondition ends the match when either player reaches 0 or less life.
 */
public class StandardWinCondition implements WinCondition {
    @Override
    public boolean checkWin(GameState state) {
        return state.getPlayer1Life() <= 0 || state.getPlayer2Life() <= 0;
    }
}
