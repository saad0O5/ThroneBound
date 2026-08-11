package network;

import engine.Player;

/**
 * AssignPlayerMessage informs a client which `Player` they are.
 */
public class AssignPlayerMessage extends Message {
    private final Player assigned;

    public AssignPlayerMessage(Player assigned) {
        super("ASSIGN_PLAYER");
        this.assigned = assigned;
    }

    public Player getAssigned() { return assigned; }
}
