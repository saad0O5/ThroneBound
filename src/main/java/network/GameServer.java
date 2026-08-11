package network;

import engine.GameState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
    private final engine.TurnManager turnManager;
    private volatile ServerSocket serverSocket;
    private boolean hasLocalHost = false;
    private boolean hostReady = false;
    private boolean remoteReady = false;
    private Runnable onMatchStart;
    private final Map<engine.Player, SetupInfo> playerSetup = new HashMap<>();

    public GameServer(int port) {
        this(port, new GameState());
    }

    /** Lets a caller (e.g. tests) inject a specific GameState instance. */
    public GameServer(int port, GameState gameState) {
        this.port = port;
        this.clients = new CopyOnWriteArrayList<>();
        this.gameState = gameState;
        this.turnManager = new engine.TurnManager(new engine.StandardWinCondition());
    }

    public boolean hasLocalHost() { return hasLocalHost; }
    public void setHasLocalHost(boolean hasLocalHost) { this.hasLocalHost = hasLocalHost; }
    public void setOnMatchStart(Runnable callback) { this.onMatchStart = callback; }

    public int getPort() { return port; }
    public List<ClientHandler> getClients() { return clients; }
    public GameState getGameState() { return gameState; }

    public engine.TurnManager getTurnManager() { return turnManager; }

    /**
     * Check configured win condition and mark the game over if met.
     * Broadcasts final state to clients when match ends.
     */
    public synchronized void checkAndHandleWin() {
        if (gameState.isMatchOver()) return;
        if (turnManager.checkWinCondition(gameState)) {
            gameState.setMatchOver(true);
            broadcast(new StateUpdateMessage(network.GameStateSnapshot.fromGameState(gameState)));
            // Optionally broadcast a specific end-match message in future
        }
    }

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

    public synchronized void recordPlayerSetup(engine.Player player, String factionName, java.util.List<String> deckCardNames) {
        playerSetup.put(player, new SetupInfo(factionName, deckCardNames));
    }

    public synchronized void markPlayerReady(engine.Player player) {
        if (player == engine.Player.PLAYER1) {
            hostReady = true;
        } else {
            remoteReady = true;
        }
        if (hostReady && remoteReady) {
            SetupInfo s1 = playerSetup.get(engine.Player.PLAYER1);
            SetupInfo s2 = playerSetup.get(engine.Player.PLAYER2);
            boolean valid = s1 != null && s2 != null && s1.deckCards != null && s2.deckCards != null && s1.deckCards.size() == 12 && s2.deckCards.size() == 12 && s1.factionName != null && s2.factionName != null;
            if (!valid) return;
            if (onMatchStart != null) {
                onMatchStart.run();
            }
            broadcast(new StartMatchMessage());
            broadcast(new StateUpdateMessage(GameStateSnapshot.fromGameState(gameState)));
        }
    }

    private static final class SetupInfo {
        final String factionName;
        final java.util.List<String> deckCards;

        SetupInfo(String factionName, java.util.List<String> deckCards) {
            this.factionName = factionName;
            this.deckCards = deckCards;
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

    public synchronized void handleForfeit(engine.Player player, String reason) {
        if (gameState.isMatchOver()) return;
        gameState.setMatchOver(true);
        broadcast(new ForfeitMessage(reason));
        broadcast(new StateUpdateMessage(GameStateSnapshot.fromGameState(gameState)));
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
