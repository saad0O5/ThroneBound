package network;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test derived from the UML networking relationships:
 * GameServer "1" o-- "1..*" ClientHandler, and GameClient sends/receives
 * Messages to/from it.
 *
 * This is the most useful test to run manually as you implement
 * GameServer/ClientHandler/GameClient — it exercises the real
 * client-server flow on localhost rather than mocking sockets.
 *
 * RED until start()/connect()/sendMessage()/listen() are implemented.
 * Once GameServer.start() and GameClient.connect() work, this should
 * connect successfully within the timeout below.
 */
class NetworkIntegrationTest {

    @Test
    @Timeout(5)
    void clientCanConnectToLocalServer() {
        int testPort = 5555;
        GameServer server = new GameServer(testPort);

        Thread serverThread = new Thread(server::start);
        serverThread.setDaemon(true);
        serverThread.start();

        GameClient client = new GameClient();
        // TODO: once implemented, this should not throw, and server.getClients()
        // should grow to size 1 shortly after connecting
        assertDoesNotThrow(() -> client.connect("localhost", testPort));
    }

    // TODO once messaging works end-to-end, add:
    //   - a test that a PlayCardMessage sent by GameClient is received by
    //     the server's ClientHandler
    //   - a test that GameServer.broadcast() reaches both connected clients
}
