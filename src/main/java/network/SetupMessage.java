package network;

import java.io.Serializable;
import java.util.List;

/**
 * SetupMessage carries a player's chosen faction and deck (by name) to the server.
 */
public class SetupMessage extends Message implements Serializable {
    private final String factionName;
    private final List<String> deckCardNames;

    public SetupMessage(String factionName, List<String> deckCardNames) {
        super("SETUP");
        this.factionName = factionName;
        this.deckCardNames = deckCardNames;
    }

    public String getFactionName() { return factionName; }
    public List<String> getDeckCardNames() { return deckCardNames; }
}
