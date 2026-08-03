package network;

/**
 * GameServer
 * Owner: Member A (Networking & Concurrency)
 *
 * Responsibility:
 *   - Opens a ServerSocket on a configurable port and listens for incoming connections.
 *   - Accepts exactly 2 client connections for a 1v1 match (per locked-in project parameters).
 *   - Spawns one ClientHandler thread per connected client.
 *   - Holds the shared GameState instance both ClientHandlers read/write.
 *
 * TODO:
 *   - [ ] Implement start(): open socket, loop accepting connections, launch ClientHandler threads
 *   - [ ] Implement broadcast(Message): send a message to both connected clients
 *   - [ ] Decide how the match's WinCondition (Standard vs Timed) is passed in from the host's
 *         match-setup choice (see engine.WinCondition)
 *   - [ ] Handle a client disconnecting mid-match (forfeit / graceful shutdown)
 *   - [ ] Add basic connection logging for debugging
 */
public class GameServer {
    // TODO: fields - port, List<ClientHandler> clients, GameState gameState

    // TODO: constructor

    // TODO: public void start()

    // TODO: public void broadcast(Message message)
}
