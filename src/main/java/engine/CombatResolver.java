package engine;

import cards.Card;
import java.util.List;

/**
 * CombatResolver applies one-sided or mutual combat damage between cards.
 */
public class CombatResolver {
    public void resolveAttack(Card attacker, Card defender) {
        resolveAttack(attacker, defender, null);
    }

    public void resolveAttack(Card attacker, Card defender, GameState state) {
        if (attacker == null || defender == null) {
            return;
        }

        int incomingDamage = attacker.getAttack();
        int updatedHealth = defender.getHealth() - incomingDamage;
        defender.setHealth(updatedHealth);
    }

    public void resolveMutualAttack(Card attacker, Card defender, GameState state, List<Card> diedThisRound) {
        if (attacker == null || defender == null) {
            return;
        }

        int attackerHealthAfter = attacker.getHealth() - defender.getAttack();
        int defenderHealthAfter = defender.getHealth() - attacker.getAttack();

        attacker.setHealth(attackerHealthAfter);
        defender.setHealth(defenderHealthAfter);

        if (attackerHealthAfter <= 0) {
            diedThisRound.add(attacker);
        }
        if (defenderHealthAfter <= 0) {
            diedThisRound.add(defender);
        }
    }
}
