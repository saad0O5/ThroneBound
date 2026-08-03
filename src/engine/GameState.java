package engine;

/**
 * GameState
 * Owner: Member B (Game Engine)
 *
 * Responsibility:
 *   - The single shared source of truth for an in-progress match: both players'
 *     Life Totals, both players' Lanes, whose turn it is, and both ResourcePools.
 *   - THIS IS THE PROJECT'S CONCURRENCY HOTSPOT: both ClientHandler threads read
 *     and write this object concurrently. Every mutating method must be properly
 *     synchronized (synchronized methods, or a ReentrantLock) so two simultaneous
 *     actions from both players can never corrupt state.
 *
 * TODO:
 *   - [ ] Fields: player1Life, player2Life, lanesP1 (List<Lane>, size 3),
 *         lanesP2 (List<Lane>, size 3), currentTurn, resourcePoolP1, resourcePoolP2
 *   - [ ] Implement synchronized playCard(Card card, int laneIndex) — validate resources
 *         via ResourcePool.canAfford(), place card, deduct cost
 *   - [ ] Implement synchronized endTurn() — hand off to TurnManager.nextTurn()
 *   - [ ] Implement synchronized resolveCombat() — delegate to CombatResolver
 *   - [ ] Add a way to produce a snapshot/copy of state for StateUpdateMessage
 *   - [ ] Write a short section in ProjectReport.pdf explaining your chosen
 *         synchronization strategy and why it prevents race conditions
 */
public class GameState {
    // TODO: fields

    // TODO: public synchronized void playCard(Card card, int laneIndex)

    // TODO: public synchronized void endTurn()

    // TODO: public synchronized void resolveCombat()
}
