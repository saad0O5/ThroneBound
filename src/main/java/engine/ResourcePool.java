package engine;

import cards.Cost;

/**
 * ResourcePool tracks the resources available to a player.
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

    public boolean canAfford(Cost cost) {
        return essence >= cost.getEssence()
                && mana >= cost.getMana()
                && soul >= cost.getSoul();
    }

    public void deduct(Cost cost) {
        if (!canAfford(cost)) {
            throw new IllegalStateException("Attempted to deduct unaffordable cost");
        }
        essence -= cost.getEssence();
        mana -= cost.getMana();
        soul -= cost.getSoul();
        if (essence < 0) essence = 0;
        if (mana < 0) mana = 0;
        if (soul < 0) soul = 0;
    }

    public void accumulate() {
        essence += 1;
        mana += 1;
        soul += 1;
    }

    public void addBonusMana(int bonus) {
        mana += bonus;
    }

    public void setEssence(int essence) {
        this.essence = essence;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setSoul(int soul) {
        this.soul = soul;
    }
}
