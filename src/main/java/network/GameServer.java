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
    private boolean hasLocalHost = false;
    private boolean hostReady = false;
    private boolean remoteReady = false;
    private Runnable onMatchStart;

    public GameServer(int port) {
        this(port, new GameState());
    }

    /** Lets a caller (e.g. tests) inject a specific GameState instance. */
    public GameServer(int port, GameState gameState) {
        this.port = port;
        this.clients = new CopyOnWriteArrayList<>();
        this.gameState = gameState;
    }

    public boolean hasLocalHost() { return hasLocalHost; }
    public void setHasLocalHost(boolean hasLocalHost) { this.hasLocalHost = hasLocalHost; }
    public void setOnMatchStart(Runnable callback) { this.onMatchStart = callback; }

    public int getPort() { return port; }
    public List<ClientHandler> getClients() { return clients; }
    public GameState getGameState() { return gameState; }

    /** Blocks accepting connections until MAX_PLAYERS clients have joined. */
    public void start() {
        try {
            ServerSocket socket = new ServerSocket(port);
            this.serverSocket = socket;
            while (clients.size() + (hasLocalHost ? 1 : 0) < MAX_PLAYERS) {
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

    /** Called by ClientHandler when its streams are initialized. */
    public synchronized void clientReady(ClientHandler handler) {
        int index = clients.indexOf(handler);
        engine.Player assigned;
        if (hasLocalHost) {
            assigned = (index == 0) ? engine.Player.PLAYER2 : engine.Player.PLAYER1;
        } else {
            assigned = (index == 0) ? engine.Player.PLAYER1 : engine.Player.PLAYER2;
        }
        try {
            handler.sendMessage(new AssignPlayerMessage(assigned));
        } catch (RuntimeException ignored) { }
    }

    public synchronized void markPlayerReady(engine.Player player) {
        if (player == engine.Player.PLAYER1) {
            hostReady = true;
        } else {
            remoteReady = true;
        }
        if (hostReady && remoteReady) {
            if (onMatchStart != null) {
                onMatchStart.run();
            }
            broadcast(new StartMatchMessage());
            broadcast(new StateUpdateMessage(GameStateSnapshot.fromGameState(gameState)));
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
        for (ClientHandler client : List.copyOf(clients)) {
            try {
                client.sendMessage(message);
            } catch (RuntimeException e) {
                System.err.println("GameServer: failed to send to a client (removing): " + e.getMessage());
                clients.remove(client);
                client.close();
            }
        }
    }
}
