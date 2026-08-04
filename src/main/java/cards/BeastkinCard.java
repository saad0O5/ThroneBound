package cards;

import engine.GameState;

/**
 * BeastkinCard (abstract)
 * Owner: Member B
 *
 * Shared base for Beastkin Clans cards — implements the "Pack" synergy pattern.
 * Concrete cards (e.g. Wolf Pup, Alpha Wolf — see docs/Card_List.md) extend this
 * and provide their own play() effect using countOtherBeastkinInPlay() as a helper.
 *
 * TODO:
 *   - [ ] Implement countOtherBeastkinInPlay(GameState) — used by most Beastkin
 *         cards' Pack bonus conditions
 *   - [ ] Create the 15 individual Beastkin card subclasses from docs/Card_List.md
 */
public abstract class BeastkinCard extends Card {
    protected BeastkinCard(String name, Cost cost, int attack, int health) {
        super(name, cost, attack, health);
    }

    /** TODO: count other Beastkin cards this player currently has in play. */
    protected int countOtherBeastkinInPlay(GameState state) {
        throw new UnsupportedOperationException("TODO: implement Pack-count helper");
    }
}
