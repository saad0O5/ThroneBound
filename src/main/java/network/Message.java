package network;

import java.io.Serializable;

/**
 * Message (abstract base class)
 * Owner: Member A (Networking & Concurrency)
 *
 * Fully implemented at this base level — no logic to fill in here.
 * See PlayCardMessage / EndTurnMessage / StateUpdateMessage for the
 * concrete message types.
 */
public abstract class Message implements Serializable {
    private final String type;

    protected Message(String type) {
        this.type = type;
    }

    public String getType() { return type; }
}
