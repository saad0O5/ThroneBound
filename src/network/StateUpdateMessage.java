package network;

/**
 * StateUpdateMessage
 * Owner: Member A (Networking & Concurrency)
 * Extends: Message
 *
 * Responsibility:
 *   - Sent from server to both clients after any GameState change, carrying enough
 *     info for each client's GUI to redraw (or the full GameState snapshot, simplest
 *     to start with).
 *
 * TODO:
 *   - [ ] Decide: send the full GameState snapshot each time (simpler, more bandwidth)
 *         vs a diff/delta (more complex, less bandwidth) — full snapshot recommended
 *         given LAN-only scope and small state size
 *   - [ ] Fields: GameState snapshot (or relevant subset)
 *   - [ ] Constructor + getters
 */
public class StateUpdateMessage /* extends Message */ {
    // TODO
}
