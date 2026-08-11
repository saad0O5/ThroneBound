package network;

/**
 * ErrorMessage carries a server-side error back to a client.
 */
public class ErrorMessage extends Message {
    private final String error;

    public ErrorMessage(String error) {
        super("ERROR");
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
