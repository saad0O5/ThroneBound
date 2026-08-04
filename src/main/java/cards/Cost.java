package cards;

/**
 * Cost
 * Owner: Member B (Cards/Factions)
 *
 * Simple data holder for a card's resource cost (Essence / Mana / Soul).
 * Fully implemented — no game logic here, just data.
 */
public class Cost {
    private final int essence;
    private final int mana;
    private final int soul;

    public Cost(int essence, int mana, int soul) {
        this.essence = essence;
        this.mana = mana;
        this.soul = soul;
    }

    public int getEssence() { return essence; }
    public int getMana() { return mana; }
    public int getSoul() { return soul; }
}
