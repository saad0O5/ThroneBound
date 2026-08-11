package cards;

import engine.GameState;

/**
 * ArcaneCard (abstract)
 * Owner: Member B
 *
 * Shared base for Arcane Order cards (creatures and spells).
 */
public class ArcaneCard extends Card {

    protected ArcaneCard(String name, Cost cost, int attack, int health) {
        super(name, cost, attack, health);
    }

    protected ArcaneCard(String name, Cost cost, int attack, int health, boolean spell) {
        super(name, cost, attack, health, spell);
    }

    @Override
    public void play(GameState state) {
        // Default arcane card effect is no-op unless overridden.
    }
}