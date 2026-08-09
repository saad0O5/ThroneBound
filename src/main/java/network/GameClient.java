package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * GameClient
 * Owner: Member A (Networking & Concurrency)
 */
public class GameClient {
    private static final int MAX_CONNECT_ATTEMPTS = 20;
    private static final long RETRY_DELAY_MS = 100;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean listening;
    private Consumer<Message> messageListener;

    /**
     * Registers a callback invoked on the background listener thread (see
     * listen()) whenever a Message arrives from the server. This class
     * deliberately doesn't import JavaFX — whoever wires this into
     * MatchScreen (implementing GameStateObserver) is responsible for
     * hopping onto the JavaFX thread with Platform.runLater(...) before
     * touching UI state, per that interface's own TODO.
     */
    public void setMessageListener(Consumer<Message> listener) {
        this.messageListener = listener;
    }

    /**
     * Retries briefly (up to ~2s) in case the server's ServerSocket hasn't
     * finished binding yet — relevant when server.start() and client.connect()
     * are kicked off close together, as in NetworkIntegrationTest.
     */
    public void connect(String host, int port) {
        IOException lastError = null;
        for (int attempt = 0; attempt < MAX_CONNECT_ATTEMPTS; attempt++) {
            try {
                socket = new Socket(host, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                return;
            } catch (IOException e) {
                lastError = e;
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        throw new RuntimeException("Failed to connect to " + host + ":" + port, lastError);
    }

    public synchronized void sendMessage(Message message) {
        if (out == null) {
            throw new IllegalStateException("Not connected — call connect() first");
        }
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    /** Starts a daemon background thread reading Messages until disconnected. */
    public void listen() {
        if (in == null) {
            throw new IllegalStateException("Not connected — call connect() first");
        }
        listening = true;
        Thread listenerThread = new Thread(() -> {
            try {
                while (listening) {
                    Message message = (Message) in.readObject();
                    if (messageListener != null) {
                        messageListener.accept(message);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("GameClient: disconnected from server (" + e.getMessage() + ")");
            } finally {
                listening = false;
            }
        }, "GameClient-Listener");
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void disconnect() {
        listening = false;
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {
        }
    }
}
