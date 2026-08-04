package engine;

import cards.Card;
import java.util.ArrayList;
import java.util.List;

/**
 * GameState
 * Owner: Member B (Game Engine)
 *
 * THIS IS THE PROJECT'S CONCURRENCY HOTSPOT — see engine/GameStateTest.java's
 * concurrency test. All mutating methods below are marked synchronized;
 * keep them that way as you implement, and do not add new state-mutating
 * methods without synchronizing them too.
 *
 * Initial state (already implemented, verified by GameStateTest):
 *   - both players start at 25 Life (locked-in project parameter)
 *   - both players have exactly 3 lanes (locked-in project parameter)
 *   - Player 1 goes first
 *
 * TODO:
 *   - [ ] Implement playCard(Card, int laneIndex)
 *   - [ ] Implement endTurn()
 *   - [ ] Implement resolveCombat()
 */
public class GameState {
    public static final int STARTING_LIFE = 25;
    public static final int LANES_PER_PLAYER = 3;

    private int player1Life;
    private int player2Life;
    private final List<Lane> lanesP1;
    private final List<Lane> lanesP2;
    private Player currentTurn;

    public GameState() {
        this.player1Life = STARTING_LIFE;
        this.player2Life = STARTING_LIFE;
        this.lanesP1 = new ArrayList<>();
        this.lanesP2 = new ArrayList<>();
        for (int i = 0; i < LANES_PER_PLAYER; i++) {
            lanesP1.add(new Lane());
            lanesP2.add(new Lane());
        }
        this.currentTurn = Player.PLAYER1;
    }

    public int getPlayer1Life() { return player1Life; }
    public int getPlayer2Life() { return player2Life; }
    public List<Lane> getLanesP1() { return lanesP1; }
    public List<Lane> getLanesP2() { return lanesP2; }
    public Player getCurrentTurn() { return currentTurn; }

    /** TODO: implement — validate cost via ResourcePool, place card, deduct cost. */
    public synchronized void playCard(Card card, int laneIndex) {
        throw new UnsupportedOperationException("TODO: implement playCard()");
    }

    /** TODO: implement — hand off to TurnManager.nextTurn(). */
    public synchronized void endTurn() {
        throw new UnsupportedOperationException("TODO: implement endTurn()");
    }

    /** TODO: implement — delegate to CombatResolver. */
    public synchronized void resolveCombat() {
        throw new UnsupportedOperationException("TODO: implement resolveCombat()");
    }
}
