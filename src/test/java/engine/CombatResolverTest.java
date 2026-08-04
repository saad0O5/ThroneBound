package engine;

import cards.Cost;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: CombatResolver.resolveAttack(Card, Card)
 * applies the attacker's Attack stat as damage to the defender's Health.
 */
class CombatResolverTest {

    @Test
    void attackerDamagesDefenderByItsAttackStat() {
        CombatResolver resolver = new CombatResolver();
        cards.Card attacker = new TestCards.SimpleCard("Attacker", new Cost(1, 0, 0), 3, 5);
        cards.Card defender = new TestCards.SimpleCard("Defender", new Cost(1, 0, 0), 1, 5);

        resolver.resolveAttack(attacker, defender);

        assertEquals(2, defender.getHealth()); // 5 - 3 attack damage
    }

    @Test
    void defenderDiesWhenHealthReachesZero() {
        CombatResolver resolver = new CombatResolver();
        cards.Card attacker = new TestCards.SimpleCard("Attacker", new Cost(1, 0, 0), 10, 5);
        cards.Card defender = new TestCards.SimpleCard("Defender", new Cost(1, 0, 0), 1, 5);

        resolver.resolveAttack(attacker, defender);

        assertTrue(defender.getHealth() <= 0);
    }
}
