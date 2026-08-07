package cards;

/**
 * ArcaneCard (abstract)
 * Owner: Member B
 *
 * Shared base for Arcane Order cards (creatures and spells).
 *
 * TODO:
 *   - [ ] Create the 15 individual Arcane Order card subclasses from docs/Card_List.md
 */
public abstract class ArcaneCard extends Card {

    protected ArcaneCard(String name, Cost cost, int attack, int health) {
        super(name, cost, attack, health);
    }
}