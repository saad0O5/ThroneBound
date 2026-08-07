package cards;

/**
 * UndeadCard (abstract)
 * Owner: Member B
 *
 * Shared base for Undead Legion cards — most define an onDeath() effect.
 */
public abstract class UndeadCard extends Card {
    protected UndeadCard(String name, Cost cost, int attack, int health) {
        super(name, cost, attack, health);
    }

    protected UndeadCard(String name, Cost cost, int attack, int health, boolean spell) {
        super(name, cost, attack, health, spell);
    }
}
