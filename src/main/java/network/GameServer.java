package network;

import engine.GameState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GameServer
 * Owner: Member A (Networking & Concurrency)
 */
public class GameServer {
    /** Locked-in project parameter: 1v1 only. */
    public static final int MAX_PLAYERS = 2;

    private final int port;
    private final List<ClientHandler> clients;
    private final GameState gameState;
    private volatile ServerSocket serverSocket;

    public GameServer(int port) {
        this(port, new GameState());
    }

    /** Lets a caller (e.g. tests) inject a specific GameState instance. */
    public GameServer(int port, GameState gameState) {
        this.port = port;
        this.clients = new CopyOnWriteArrayList<>();
        this.gameState = gameState;
    }

    public int getPort() { return port; }
    public List<ClientHandler> getClients() { return clients; }
    public GameState getGameState() { return gameState; }

    /** Blocks accepting connections until MAX_PLAYERS clients have joined. */
    public void start() {
        try {
            ServerSocket socket = new ServerSocket(port);
            this.serverSocket = socket;
            while (clients.size() < MAX_PLAYERS) {
                Socket clientSocket = socket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this);
                clients.add(handler);

                Thread thread = new Thread(handler, "ClientHandler-" + clients.size());
                thread.setDaemon(true);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException("GameServer failed to start on port " + port, e);
        }
    }

    public void stop() {
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException ignored) {
        }
        for (ClientHandler handler : clients) {
            handler.close();
        }
    }

    public void broadcast(Message message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}
