package cards;

/**
 * UndeadCard (abstract)
 * Owner: Member B
 *
 * Shared base for Undead Legion cards — most define an onDeath() effect.
 *
 * TODO:
 *   - [ ] Create the 15 individual Undead Legion card subclasses from docs/Card_List.md,
 *         overriding onDeath() where the card has a death trigger
 */
public abstract class UndeadCard extends Card {
    protected UndeadCard(String name, Cost cost, int attack, int health) {
        super(name, cost, attack, health);
    }
}
