package engine;

import cards.Card;
import java.util.ArrayList;
import java.util.List;

/**
 * GameState is the central board state for the game. It owns the lanes,
 * life totals, resources, turn, and graveyard.
 */
public class GameState {
    public static final int STARTING_LIFE = 25;
    public static final int LANES_PER_PLAYER = 3;

    private int player1Life;
    private int player2Life;
    private final List<Lane> lanesP1;
    private final List<Lane> lanesP2;
    private final List<Card> graveyard;
    private final ResourcePool resourcesP1;
    private final ResourcePool resourcesP2;
    private Player currentTurn;

    public GameState() {
        this.player1Life = STARTING_LIFE;
        this.player2Life = STARTING_LIFE;
        this.lanesP1 = new ArrayList<>();
        this.lanesP2 = new ArrayList<>();
        this.graveyard = new ArrayList<>();
        this.resourcesP1 = new ResourcePool(5, 5, 5);
        this.resourcesP2 = new ResourcePool(5, 5, 5);
        for (int i = 0; i < LANES_PER_PLAYER; i++) {
            lanesP1.add(new Lane());
            lanesP2.add(new Lane());
        }
        this.currentTurn = Player.PLAYER1;
    }

    public int getPlayer1Life() { return player1Life; }
    public int getPlayer2Life() { return player2Life; }
    public List<Lane> getLanesP1() { return lanesP1; }
    public List<Lane> getLanesP2() { return lanesP2; }
    public List<Card> getGraveyard() { return graveyard; }
    public ResourcePool getResourcesP1() { return resourcesP1; }
    public ResourcePool getResourcesP2() { return resourcesP2; }
    public Player getCurrentTurn() { return currentTurn; }

    public synchronized void playCard(Card card, int laneIndex) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        ResourcePool currentResources = currentTurn == Player.PLAYER1 ? resourcesP1 : resourcesP2;
        if (!currentResources.canAfford(card.getCost())) {
            throw new IllegalStateException("Player cannot afford this card");
        }

        if (!card.isSpell()) {
            if (laneIndex < 0 || laneIndex >= LANES_PER_PLAYER) {
                throw new IllegalArgumentException("Lane index out of range");
            }
            List<Lane> targetLanes = currentTurn == Player.PLAYER1 ? lanesP1 : lanesP2;
            Lane targetLane = targetLanes.get(laneIndex);
            if (!targetLane.isEmpty()) {
                throw new IllegalStateException("Lane is already occupied");
            }
            targetLane.placeCard(card);
            card.setLane(targetLane);
        }

        card.setOwner(currentTurn);
        currentResources.deduct(card.getCost());
        card.play(this);
    }

    public synchronized void endTurn() {
        if (currentTurn == Player.PLAYER1) {
            currentTurn = Player.PLAYER2;
            resourcesP2.accumulate();
        } else {
            currentTurn = Player.PLAYER1;
            resourcesP1.accumulate();
        }
    }

    public synchronized void resolveCombat() {
        CombatResolver resolver = new CombatResolver();
        for (int i = 0; i < LANES_PER_PLAYER; i++) {
            Lane laneP1 = lanesP1.get(i);
            Lane laneP2 = lanesP2.get(i);
            Card attacker = laneP1.getOccupant();
            Card defender = laneP2.getOccupant();

            if (attacker != null && defender != null) {
                resolver.resolveAttack(attacker, defender, this);
                if (defender.getHealth() <= 0) {
                    laneP2.removeCard();
                    defender.setLane(null);
                    graveyard.add(defender);
                }
            }
        }
    }
}
