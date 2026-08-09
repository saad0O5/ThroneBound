package network;

import cards.ArcaneOrderFaction;
import cards.BeastkinFaction;
import cards.Card;
import cards.Faction;
import cards.UndeadLegionFaction;
import engine.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * ClientHandler
 * Owner: Member A (Networking & Concurrency)
 *
 * Runs on its own thread per connected client. Reads Messages off the
 * socket and applies them to the shared GameState via its synchronized
 * methods (playCard / endTurn). After a successful mutation, broadcasts a
 * StateUpdateMessage snapshot back to both clients.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final GameServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /** Standalone constructor (no server wiring) — kept for flexibility/tests. */
    public ClientHandler(Socket socket) {
        this(socket, null);
    }

    public ClientHandler(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    public Socket getSocket() { return socket; }

    @Override
    public void run() {
        try {
            // Output stream must be created (and its header flushed) before
            // the input stream on either side of the socket — otherwise both
            // ends can block waiting to read the other's ObjectInputStream
            // header. Same order is used in GameClient.connect().
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Message message = (Message) in.readObject();
                handle(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ClientHandler: client disconnected (" + e.getMessage() + ")");
        } finally {
            close();
        }
    }

    private void handle(Message message) {
        if (server == null) return; // standalone handler, nothing to apply to

        GameState gameState = server.getGameState();

        if (message instanceof PlayCardMessage playCard) {
            Card card = resolveCard(playCard.getCardName());
            gameState.playCard(card, playCard.getLaneIndex());
        } else if (message instanceof EndTurnMessage) {
            gameState.endTurn();
        } else {
            return; // StateUpdateMessage or anything else isn't client-to-server
        }

        broadcastState(gameState);
    }

    /**
     * Cards travel over the wire by name only (server-side authoritative
     * lookup — see PlayCardMessage), so this rebuilds a fresh, real Card
     * instance server-side by scanning all three factions' pools before
     * applying it to GameState. Building the factions fresh each call
     * guarantees a brand-new (not reused/stateful) Card instance.
     */
    private Card resolveCard(String cardName) {
        List<Faction> factions = List.of(
                new BeastkinFaction(), new ArcaneOrderFaction(), new UndeadLegionFaction()
        );
        for (Faction faction : factions) {
            for (Card card : faction.getCardPool()) {
                if (card.getName().equals(cardName)) {
                    return card;
                }
            }
        }
        throw new IllegalArgumentException("Unknown card name: " + cardName);
    }

    /**
     * Minimal state snapshot for now (life totals + whose turn it is).
     * Extend once lane/card representation over JSON is worth the added
     * complexity — Card/Lane don't have a JSON form yet.
     */
    private void broadcastState(GameState gameState) {
        String json = String.format(
                "{\"player1Life\":%d,\"player2Life\":%d,\"currentTurn\":\"%s\"}",
                gameState.getPlayer1Life(),
                gameState.getPlayer2Life(),
                gameState.getCurrentTurn()
        );
        server.broadcast(new StateUpdateMessage(json));
    }

    public synchronized void sendMessage(Message message) {
        if (out == null) {
            throw new IllegalStateException("ClientHandler hasn't started yet (run() not called)");
        }
        try {
            out.writeObject(message);
            out.flush();
            out.reset(); // avoid stale cached object graph across successive sends
        } catch (IOException e) {
            throw new RuntimeException("Failed to send message to client", e);
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
