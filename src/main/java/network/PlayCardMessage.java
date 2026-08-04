package network;

/**
 * PlayCardMessage
 * Owner: Member A
 *
 * Fully implemented — simple data carrier, no logic required.
 */
public class PlayCardMessage extends Message {
    private final String cardName;
    private final int laneIndex;

    public PlayCardMessage(String cardName, int laneIndex) {
        super("PLAY_CARD");
        this.cardName = cardName;
        this.laneIndex = laneIndex;
    }

    public String getCardName() { return cardName; }
    public int getLaneIndex() { return laneIndex; }
}
