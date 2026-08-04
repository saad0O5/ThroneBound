package engine;

import cards.Cost;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: ResourcePool has essence/mana/soul and
 * "canAfford(Cost): boolean" / "deduct(Cost)".
 */
class ResourcePoolTest {

    @Test
    void canAffordWhenAllResourcesSufficient() {
        ResourcePool pool = new ResourcePool(3, 2, 1);
        assertTrue(pool.canAfford(new Cost(2, 1, 1)));
    }

    @Test
    void cannotAffordWhenAnyResourceInsufficient() {
        ResourcePool pool = new ResourcePool(1, 0, 0);
        assertFalse(pool.canAfford(new Cost(2, 0, 0)));
    }

    @Test
    void deductSubtractsExactCostAmounts() {
        ResourcePool pool = new ResourcePool(5, 5, 5);
        pool.deduct(new Cost(2, 1, 0));
        assertEquals(3, pool.getEssence());
        assertEquals(4, pool.getMana());
        assertEquals(5, pool.getSoul());
    }
}
