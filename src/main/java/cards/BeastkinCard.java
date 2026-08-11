package cards;

import engine.GameState;
import engine.Lane;
import engine.Player;

/**
 * BeastkinCard (abstract)
 * Owner: Member B
 *
 * Shared base for Beastkin Clans cards — implements the "Pack" synergy pattern.
 */
public class BeastkinCard extends Card {
    protected BeastkinCard(String name, Cost cost, int attack, int health) {
        super(name, cost, attack, health);
    }

    /** Count other Beastkin cards this player currently has in play. */
    protected int countOtherBeastkinInPlay(GameState state) {
        Player owner = getOwner();
        if (owner == null) {
            return 0;
        }

        int count = 0;
        for (Lane lane : state.getLanesP1()) {
            if (lane.getOccupant() != null && lane.getOccupant() != this && lane.getOccupant() instanceof BeastkinCard) {
                if (owner.equals(lane.getOccupant().getOwner())) {
                    count++;
                }
            }
        }
        for (Lane lane : state.getLanesP2()) {
            if (lane.getOccupant() != null && lane.getOccupant() != this && lane.getOccupant() instanceof BeastkinCard) {
                if (owner.equals(lane.getOccupant().getOwner())) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void play(GameState state) {
        // Default Beastkin play effect is handled by concrete cards or no-op.
    }
}
