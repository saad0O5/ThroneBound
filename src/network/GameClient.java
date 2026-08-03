package network;

/**
 * GameClient
 * Owner: Member A (Networking & Concurrency)
 *
 * Responsibility:
 *   - Runs on the joining player's machine (and conceptually on the host's too,
 *     talking to their own local GameServer).
 *   - Connects to a GameServer at a given IP/port.
 *   - Sends the local player's actions (PlayCardMessage, EndTurnMessage) to the server.
 *   - Listens for StateUpdateMessages on a background thread and hands them to the GUI.
 *
 * TODO:
 *   - [ ] Implement connect(String ip, int port): open the socket connection
 *   - [ ] Implement sendMessage(Message message)
 *   - [ ] Implement listen(): background thread reading incoming messages
 *   - [ ] IMPORTANT: when a StateUpdateMessage arrives, hand it to the GUI via
 *         Platform.runLater(...) — never touch JavaFX nodes directly from this thread
 *   - [ ] Handle connection failure (bad IP/port, server not running) with a clear error
 *         the HostJoinScreen can display
 */
public class GameClient {
    // TODO: fields - Socket socket

    // TODO: public void connect(String ip, int port)

    // TODO: public void sendMessage(Message message)

    // TODO: public void listen()
}
