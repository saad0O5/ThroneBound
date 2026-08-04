package network;

import java.util.ArrayList;
import java.util.List;

/**
 * GameServer
 * Owner: Member A (Networking & Concurrency)
 *
 * TODO:
 *   - [ ] Implement start(): open a ServerSocket, accept exactly 2 client
 *         connections (1v1, locked-in parameter), spawn a ClientHandler
 *         thread per client
 *   - [ ] Implement broadcast(): send a Message to both connected clients
 *   - [ ] See network/NetworkIntegrationTest.java for the end-to-end
 *         behavior this should support once implemented
 */
public class GameServer {
    private final int port;
    private final List<ClientHandler> clients;

    public GameServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public int getPort() { return port; }
    public List<ClientHandler> getClients() { return clients; }

    /** TODO: implement. */
    public void start() {
        throw new UnsupportedOperationException("TODO: implement start()");
    }

    /** TODO: implement. */
    public void broadcast(Message message) {
        throw new UnsupportedOperationException("TODO: implement broadcast()");
    }
}
