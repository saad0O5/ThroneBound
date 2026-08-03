package gui;

/**
 * HostJoinScreen
 * Owner: Member C (GUI), integrates with Member A's networking classes
 *
 * Responsibility:
 *   - Lets the player either Host a match (starts a GameServer, shows
 *     "waiting for opponent" + connection info, and exposes the win-condition
 *     choice — Standard vs Timed — since match setup is host-only) or Join a
 *     match (enter host IP/port, connect via GameClient).
 *
 * TODO:
 *   - [ ] Build Host/Join tab or toggle layout
 *   - [ ] Host path: start network.GameServer on a background thread, display
 *         local IP/port, add the Standard/Timed WinCondition selector
 *   - [ ] Join path: IP/port input fields, Connect button -> network.GameClient.connect()
 *   - [ ] Handle connection failure with a visible error message
 *   - [ ] On both players ready, transition to a faction/deck select step,
 *         then MatchScreen
 */
public class HostJoinScreen {
    // TODO
}
