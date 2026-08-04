package cards;

import java.util.List;

/**
 * Faction (abstract base class)
 * Owner: Member B (Cards/Factions)
 *
 * TODO:
 *   - [ ] Each concrete faction (BeastkinFaction, ArcaneOrderFaction,
 *         UndeadLegionFaction) must implement buildCardPool() to return its
 *         15 real cards from docs/Card_List.md
 */
public abstract class Faction {
    private final List<Card> cardPool;

    protected Faction() {
        this.cardPool = buildCardPool();
    }

    /** TODO: return this faction's 15 cards (see docs/Card_List.md). */
    protected abstract List<Card> buildCardPool();

    public List<Card> getCardPool() { return cardPool; }
}
