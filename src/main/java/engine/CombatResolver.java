package engine;

import cards.Card;

/**
 * CombatResolver applies one-sided combat damage from an attacker into a defender.
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

        if (updatedHealth <= 0 && state != null) {
            defender.onDeath(state);
        }
    }
}
