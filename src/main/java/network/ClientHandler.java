package network;

import java.net.Socket;

/**
 * ClientHandler
 * Owner: Member A (Networking & Concurrency)
 * Should implement Runnable once run() is implemented.
 *
 * TODO:
 *   - [ ] Implement Runnable, implement run(): loop reading Messages from
 *         the socket and applying them to the shared GameState via its
 *         synchronized methods
 *   - [ ] Implement sendMessage(): write a Message back to this client
 */
public class ClientHandler {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() { return socket; }

    /** TODO: implement (make this class implement Runnable first). */
    public void run() {
        throw new UnsupportedOperationException("TODO: implement run()");
    }

    /** TODO: implement. */
    public void sendMessage(Message message) {
        throw new UnsupportedOperationException("TODO: implement sendMessage()");
    }
}
