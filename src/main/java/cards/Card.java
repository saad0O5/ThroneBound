package cards;

import engine.GameState;
import engine.Lane;
import engine.Player;

/**
 * Card (abstract base class)
 * Owner: Member B (Game Engine & Cards/Factions)
 *
 * Supports both creatures and one-shot spells. Spells are represented by the
 * spell flag and do not need to occupy a lane.
 */
public abstract class Card {

    private final String name;
    private final Cost cost;
    private final int attack;
    private int health;
    private final boolean spell;
    private Player owner;
    private Lane lane;

    protected Card(String name, Cost cost, int attack, int health) {
        this(name, cost, attack, health, false);
    }

    protected Card(String name, Cost cost, int attack, int health, boolean spell) {
        this.name = name;
        this.cost = cost;
        this.attack = attack;
        this.health = health;
        this.spell = spell;
    }

    public String getName() { return name; }
    public Cost getCost() { return cost; }
    public int getAttack() { return attack; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public boolean isSpell() { return spell; }
    public boolean isCreature() { return !spell; }
    public Player getOwner() { return owner; }
    public void setOwner(Player owner) { this.owner = owner; }
    public Lane getLane() { return lane; }
    public void setLane(Lane lane) { this.lane = lane; }

    /** Implement per-card effect in each concrete subclass. */
    public abstract void play(GameState state);

    /** Default: no death effect. Override in cards that need one. */
    public void onDeath(GameState state) {
        // no-op by default
    }
}