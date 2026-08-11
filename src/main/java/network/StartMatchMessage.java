package network;

/**
 * StartMatchMessage indicates both players are ready and the match may begin.
 */
public class StartMatchMessage extends Message {
    public StartMatchMessage() {
        super("START_MATCH");
    }
}
