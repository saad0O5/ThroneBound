package network;

/**
 * ForfeitMessage indicates a player disconnected/forfeited the match.
 */
public class ForfeitMessage extends Message {
    private final String reason;

    public ForfeitMessage(String reason) {
        super("FORFEIT");
        this.reason = reason;
    }

    public String getReason() { return reason; }
}
