package network;

/**
 * EndTurnMessage
 * Owner: Member A
 *
 * Fully implemented — no extra fields needed beyond the message type.
 */
public class EndTurnMessage extends Message {
    public EndTurnMessage() {
        super("END_TURN");
    }
}
