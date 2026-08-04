package cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: "Faction 'o--' 15 Card" — each concrete
 * faction's card pool should contain exactly 15 cards once implemented.
 *
 * RED until buildCardPool() is implemented in each faction class.
 */
class FactionTest {

    @Test
    void beastkinFactionHasFifteenCards() {
        Faction faction = new BeastkinFaction();
        assertEquals(15, faction.getCardPool().size());
    }

    @Test
    void arcaneOrderFactionHasFifteenCards() {
        Faction faction = new ArcaneOrderFaction();
        assertEquals(15, faction.getCardPool().size());
    }

    @Test
    void undeadLegionFactionHasFifteenCards() {
        Faction faction = new UndeadLegionFaction();
        assertEquals(15, faction.getCardPool().size());
    }
}
