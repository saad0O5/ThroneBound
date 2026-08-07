package cards;

import engine.GameState;

/**
 * Card (abstract base class)
 * Owner: Member B (Game Engine & Cards/Factions)
 *
 * TODO (see tests in cards/CardTest.java and per-faction test files):
 *   - [ ] Subclasses must implement play(GameState) with their faction-specific effect
 *   - [ ] Subclasses may override onDeath(GameState) for death-trigger effects
 *         (default is a no-op — most Beastkin/Arcane cards don't need one)
 */
public abstract class Card {

    private final String name;
    private final Cost cost;
    private final int attack;
    private int health;

    protected Card(String name, Cost cost, int attack, int health) {
        this.name = name;
        this.cost = cost;
        this.attack = attack;
        this.health = health;
    }

    public String getName() { return name; }
    public Cost getCost() { return cost; }
    public int getAttack() { return attack; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    /** TODO: implement per-card effect in each concrete subclass. */
    public abstract void play(GameState state);

    /** Default: no death effect. Override in Undead-style cards that need one. */
    public void onDeath(GameState state) {
        // no-op by default
    }
}