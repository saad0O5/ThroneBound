package engine;

/**
 * ResourcePool
 * Owner: Member B (Game Engine)
 *
 * Responsibility:
 *   - Tracks one player's accumulated Essence / Mana / Soul (the three resource
 *     types cards cost combinations of).
 *   - Each player has their own ResourcePool; accumulation rate depends on which
 *     cards have already been played this match (per the game design doc).
 *
 * TODO:
 *   - [ ] Fields: essence (int), mana (int), soul (int)
 *   - [ ] Implement canAfford(Cost cost): boolean check against a card's cost
 *   - [ ] Implement deduct(Cost cost): subtract on successful play
 *   - [ ] Implement the per-turn accumulation logic (called from TurnManager.nextTurn())
 *   - [ ] Define the Cost type (could be its own small class: {essence, mana, soul})
 */
public class ResourcePool {
    // TODO: fields - int essence, mana, soul

    // TODO: public boolean canAfford(/* Cost cost */)

    // TODO: public void deduct(/* Cost cost */)
}
