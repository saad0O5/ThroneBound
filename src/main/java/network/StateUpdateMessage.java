package network;

import java.io.Serializable;

/**
 * StateUpdateMessage carries a serialized game state snapshot for clients.
 */
public class StateUpdateMessage extends Message {
    private final GameStateSnapshot statePayload;

    public StateUpdateMessage(GameStateSnapshot statePayload) {
        super("STATE_UPDATE");
        this.statePayload = statePayload;
    }

    public GameStateSnapshot getStatePayload() { return statePayload; }
}
