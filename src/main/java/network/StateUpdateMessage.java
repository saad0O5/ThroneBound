package network;

/**
 * StateUpdateMessage
 * Owner: Member A
 *
 * TODO:
 *   - [ ] Decide the snapshot payload type (currently a placeholder String —
 *         replace with a real GameState snapshot / DTO once GameState is
 *         serializable, or keep as a JSON string if that's your chosen
 *         wire format)
 */
public class StateUpdateMessage extends Message {
    private final String statePayload;

    public StateUpdateMessage(String statePayload) {
        super("STATE_UPDATE");
        this.statePayload = statePayload;
    }

    public String getStatePayload() { return statePayload; }
}
