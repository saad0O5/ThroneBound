package network;

import java.io.Serializable;

/**
 * StateUpdateMessage carries a serialized game state snapshot for clients.
 */
public class StateUpdateMessage extends Message {
    private final Object statePayload;

    public StateUpdateMessage(GameStateSnapshot statePayload) {
        super("STATE_UPDATE");
        this.statePayload = statePayload;
    }

    public StateUpdateMessage(String jsonPayload) {
        super("STATE_UPDATE");
        this.statePayload = jsonPayload;
    }

    /**
     * Returns either a `GameStateSnapshot` or a raw JSON `String` depending
     * on how the message was constructed. Callers should check the type.
     */
    public Object getStatePayload() { return statePayload; }
}
