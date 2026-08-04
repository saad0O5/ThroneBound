package engine;

import cards.Cost;

/**
 * ResourcePool
 * Owner: Member B (Game Engine)
 *
 * TODO:
 *   - [ ] Implement canAfford(Cost)
 *   - [ ] Implement deduct(Cost)
 *   - [ ] Implement the per-turn accumulation logic, called from TurnManager.nextTurn()
 */
public class ResourcePool {
    private int essence;
    private int mana;
    private int soul;

    public ResourcePool(int essence, int mana, int soul) {
        this.essence = essence;
        this.mana = mana;
        this.soul = soul;
    }

    public int getEssence() { return essence; }
    public int getMana() { return mana; }
    public int getSoul() { return soul; }

    /** TODO: implement — true if this pool has at least `cost`'s amount of each resource. */
    public boolean canAfford(Cost cost) {
        throw new UnsupportedOperationException("TODO: implement canAfford()");
    }

    /** TODO: implement — subtract cost's amounts; should only be called after canAfford() passes. */
    public void deduct(Cost cost) {
        throw new UnsupportedOperationException("TODO: implement deduct()");
    }
}
