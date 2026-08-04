package gui;

/**
 * MatchScreen
 * Owner: Member C (GUI), integrates with Member B's engine and Member A's networking
 * Implements: GameStateObserver
 *
 * Responsibility:
 *   - The core gameplay screen: both players' 3 lanes, Life Totals, resource
 *     bars, the active player's hand, and an End Turn button.
 *   - Registers itself as a GameStateObserver so it redraws whenever GameState
 *     changes (whether from the local player's own action or from a
 *     StateUpdateMessage arriving from the opponent over the network).
 *
 * TODO:
 *   - [ ] Build the board layout: opponent lanes/life/resources (top),
 *         player lanes/life/resources + hand (bottom)
 *   - [ ] Implement onStateChanged(GameState state) — redraw affected UI,
 *         remembering to use Platform.runLater() since updates may arrive on
 *         the network thread
 *   - [ ] Highlight playable cards in hand (enough resources) — see
 *         ResourcePool.canAfford()
 *   - [ ] Wire card clicks to send a PlayCardMessage via GameClient
 *   - [ ] Wire End Turn button to send an EndTurnMessage
 *   - [ ] Show whose turn it is
 *   - [ ] On match end, transition to ResultsScreen
 */
public class MatchScreen /* implements GameStateObserver */ {
    // TODO
}
