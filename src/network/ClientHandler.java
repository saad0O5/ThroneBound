package network;

/**
 * ClientHandler
 * Owner: Member A (Networking & Concurrency)
 * Implements: Runnable / extends Thread
 *
 * Responsibility:
 *   - One instance per connected client, running on its own thread.
 *   - Reads incoming Messages from its client's socket and applies them to the
 *     shared GameState (this is the concurrency-critical class in the project).
 *   - Sends outgoing StateUpdateMessages back to its client after state changes.
 *
 * TODO:
 *   - [ ] Implement run(): loop reading Messages from the socket's input stream
 *   - [ ] Implement sendMessage(Message): write a Message to the socket's output stream
 *   - [ ] Route incoming PlayCardMessage / EndTurnMessage to the correct GameState method
 *   - [ ] IMPORTANT: GameState mutations (playCard, endTurn, resolveCombat) must be called
 *         through GameState's synchronized methods — this is the project's core
 *         synchronization requirement, do not bypass it
 *   - [ ] Handle malformed messages / IOExceptions without crashing the server thread
 */
public class ClientHandler /* implements Runnable */ {
    // TODO: fields - Socket socket, GameState gameState

    // TODO: constructor

    // TODO: public void run()

    // TODO: public void sendMessage(Message message)
}
