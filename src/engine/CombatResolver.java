package engine;

/**
 * CombatResolver
 * Owner: Member B (Game Engine)
 *
 * Responsibility:
 *   - Resolves an attack between two cards in mirrored lanes (or a hit straight
 *     to the opponent's Life Total if the mirrored lane is empty).
 *   - Applies any on-death effects for cards that die as a result (see the Card
 *     hierarchy's death-trigger overrides, especially Undead Legion cards).
 *
 * TODO:
 *   - [ ] Implement resolveAttack(Card attacker, Card defender): apply attacker's
 *         Attack stat as damage to defender's Health, and vice versa if defender
 *         survives and counters (decide: simultaneous damage, or attacker-only?)
 *   - [ ] Trigger on-death effects when a card's Health reaches 0
 *   - [ ] Handle the "empty mirrored lane -> damage opponent Life Total directly" case
 */
public class CombatResolver {
    // TODO: public void resolveAttack(Card attacker, Card defender)
}
