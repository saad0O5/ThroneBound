package engine;

import cards.Card;
import cards.Cost;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GameState is the central board state for the game. It owns the lanes,
 * life totals, resources, turn, and graveyard.
 */
public class GameState {
    public static final int STARTING_LIFE = 25;
    public static final int LANES_PER_PLAYER = 3;
    public static final int MAX_ACTIONS_PER_TURN = 1;

    private int player1Life;
    private int player2Life;
    private int actionsThisTurn;
    private final List<Lane> lanesP1;
    private final List<Lane> lanesP2;
    private final List<Card> graveyardP1;
    private final List<Card> graveyardP2;
    private final ResourcePool resourcesP1;
    private final ResourcePool resourcesP2;
    private final EffectState player1EffectState;
    private final EffectState player2EffectState;
    private Player currentTurn;
    private boolean matchOver = false;

    public GameState() {
        this.player1Life = STARTING_LIFE;
        this.player2Life = STARTING_LIFE;
        this.lanesP1 = new ArrayList<>();
        this.lanesP2 = new ArrayList<>();
        this.graveyardP1 = new ArrayList<>();
        this.graveyardP2 = new ArrayList<>();
        this.resourcesP1 = new ResourcePool(5, 5, 5);
        this.resourcesP2 = new ResourcePool(5, 5, 5);
        this.player1EffectState = new EffectState();
        this.player2EffectState = new EffectState();
        for (int i = 0; i < LANES_PER_PLAYER; i++) {
            lanesP1.add(new Lane());
            lanesP2.add(new Lane());
        }
        this.currentTurn = Player.PLAYER1;
        this.actionsThisTurn = 0;
    }

    public int getPlayer1Life() { return player1Life; }
    public int getPlayer2Life() { return player2Life; }
    public int getActionsThisTurn() { return actionsThisTurn; }
    public List<Lane> getLanesP1() { return Collections.unmodifiableList(lanesP1); }
    public List<Lane> getLanesP2() { return Collections.unmodifiableList(lanesP2); }
    public List<Card> getGraveyard() { 
        List<Card> combined = new ArrayList<>();
        combined.addAll(graveyardP1);
        combined.addAll(graveyardP2);
        return combined;
    }

    public List<Card> getGraveyardForPlayer(Player player) {
        return player == Player.PLAYER1 ? graveyardP1 : graveyardP2;
    }
    public ResourcePool getResourcesP1() { return resourcesP1; }
    public ResourcePool getResourcesP2() { return resourcesP2; }
    public Player getCurrentTurn() { return currentTurn; }
    public EffectState getPlayer1EffectState() { return player1EffectState; }
    public EffectState getPlayer2EffectState() { return player2EffectState; }

    public synchronized void setPlayer1Life(int life) { this.player1Life = life; }
    public synchronized void setPlayer2Life(int life) { this.player2Life = life; }
    public synchronized void setCurrentTurn(Player turn) {
        this.currentTurn = turn;
        this.actionsThisTurn = 0;
    }
    public synchronized void setActionsThisTurn(int actionsThisTurn) { this.actionsThisTurn = actionsThisTurn; }
    public synchronized void setPlayer1EffectState(EffectState state) { this.player1EffectState.copyFrom(state); }
    public synchronized void setPlayer2EffectState(EffectState state) { this.player2EffectState.copyFrom(state); }

    public synchronized void playCard(Card card, int laneIndex) {
        if (matchOver) throw new IllegalStateException("Match is already over");
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (actionsThisTurn >= MAX_ACTIONS_PER_TURN) {
            throw new IllegalStateException("You can only play one card per turn.");
        }

        ResourcePool currentResources = getCurrentResources();
        EffectState effectState = getCurrentEffectState();
        Cost effectiveCost = getEffectiveCost(card, effectState);
        if (!currentResources.canAfford(effectiveCost)) {
            throw new IllegalStateException("Player cannot afford this card");
        }

        card.setOwner(currentTurn);
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

        currentResources.deduct(effectiveCost);
        if (card.isSpell()) {
            effectState.onSpellCast();
        }
        card.play(this, laneIndex);
        effectState.applyPostPlayEffects(this);
        actionsThisTurn++;
    }

    public synchronized void endTurn() {
        if (matchOver) return;
        if (currentTurn == Player.PLAYER1) {
            currentTurn = Player.PLAYER2;
            resourcesP2.accumulate();
        } else {
            currentTurn = Player.PLAYER1;
            resourcesP1.accumulate();
        }
        actionsThisTurn = 0;
    }

    public synchronized void resolveCombat() {
        if (matchOver) return;
        CombatResolver resolver = new CombatResolver();
        List<Card> diedThisRound = new ArrayList<>();

        for (int i = 0; i < LANES_PER_PLAYER; i++) {
            Lane laneP1 = lanesP1.get(i);
            Lane laneP2 = lanesP2.get(i);
            Card cardP1 = laneP1.getOccupant();
            Card cardP2 = laneP2.getOccupant();

            if (cardP1 != null && cardP2 != null) {
                resolver.resolveMutualAttack(cardP1, cardP2, this, diedThisRound);
            }
        }

        for (Card dead : diedThisRound) {
            removeCard(dead);
        }
    }

    public synchronized void removeCard(Card card) {
        if (matchOver) return;
        if (card == null) {
            return;
        }
        Lane lane = card.getLane();
        if (lane != null) {
            lane.removeCard();
            card.setLane(null);
        }
        // invoke death trigger exactly once here
        card.onDeath(this);
        // add to appropriate graveyard
        Player owner = card.getOwner();
        if (owner == Player.PLAYER1) {
            if (!graveyardP1.contains(card)) graveyardP1.add(card);
        } else if (owner == Player.PLAYER2) {
            if (!graveyardP2.contains(card)) graveyardP2.add(card);
        } else {
            // fallback: add to P1
            if (!graveyardP1.contains(card)) graveyardP1.add(card);
        }
    }

    public synchronized void clearBoard() {
        if (matchOver) return;
        for (Lane lane : lanesP1) {
            if (!lane.isEmpty()) {
                lane.getOccupant().setLane(null);
                lane.removeCard();
            }
        }
        for (Lane lane : lanesP2) {
            if (!lane.isEmpty()) {
                lane.getOccupant().setLane(null);
                lane.removeCard();
            }
        }
    }

    public synchronized boolean isMatchOver() { return matchOver; }

    public synchronized void setMatchOver(boolean over) { this.matchOver = over; }

    public synchronized void setLaneCard(Player player, int laneIndex, Card card) {
        if (laneIndex < 0 || laneIndex >= LANES_PER_PLAYER) {
            throw new IllegalArgumentException("Lane index out of range");
        }
        List<Lane> targetLanes = player == Player.PLAYER1 ? lanesP1 : lanesP2;
        Lane lane = targetLanes.get(laneIndex);
        if (!lane.isEmpty()) {
            lane.removeCard();
        }
        if (card != null) {
            lane.placeCard(card);
            card.setLane(lane);
            card.setOwner(player);
        }
    }

    public synchronized void dealDamageToPlayer(Player player, int damage) {
        if (player == Player.PLAYER1) {
            player1Life -= damage;
        } else {
            player2Life -= damage;
        }
    }

    public List<Lane> getLanesForPlayer(Player player) {
        return player == Player.PLAYER1 ? lanesP1 : lanesP2;
    }

    public List<Lane> getOpponentLanes(Player player) {
        return player == Player.PLAYER1 ? lanesP2 : lanesP1;
    }

    public ResourcePool getCurrentResources() {
        return currentTurn == Player.PLAYER1 ? resourcesP1 : resourcesP2;
    }

    public EffectState getCurrentEffectState() {
        return currentTurn == Player.PLAYER1 ? player1EffectState : player2EffectState;
    }

    private Cost getEffectiveCost(Card card, EffectState effectState) {
        int essence = Math.max(0, card.getCost().getEssence() - effectState.getBeastkinEssenceDiscount());
        int mana = card.getCost().getMana();
        if (card.isSpell()) {
            mana = Math.max(0, mana - effectState.getNextSpellManaDiscount());
        }
        int soul = card.getCost().getSoul();
        return new Cost(essence, mana, soul);
    }

    public static final class EffectState {
        private int beastkinEssenceDiscount;
        private int nextSpellManaDiscount;
        private int nextTurnManaBonus;
        private boolean extraTurnPending;

        public EffectState() { }

        void copyFrom(EffectState other) {
            if (other == null) return;
            this.beastkinEssenceDiscount = other.beastkinEssenceDiscount;
            this.nextSpellManaDiscount = other.nextSpellManaDiscount;
            this.nextTurnManaBonus = other.nextTurnManaBonus;
            this.extraTurnPending = other.extraTurnPending;
        }

        void onSpellCast() {
            this.nextSpellManaDiscount = 0;
        }

        void applyPostPlayEffects(GameState state) {
            // placeholder for persistent effects
        }

        public int getBeastkinEssenceDiscount() { return beastkinEssenceDiscount; }
        public int getNextSpellManaDiscount() { return nextSpellManaDiscount; }
        public int getNextTurnManaBonus() { return nextTurnManaBonus; }
        public boolean isExtraTurnPending() { return extraTurnPending; }

        public void setBeastkinEssenceDiscount(int discount) { this.beastkinEssenceDiscount = discount; }
        public void setNextSpellManaDiscount(int discount) { this.nextSpellManaDiscount = discount; }
        public void setNextTurnManaBonus(int bonus) { this.nextTurnManaBonus = bonus; }
        public void setExtraTurnPending(boolean extraTurnPending) { this.extraTurnPending = extraTurnPending; }
    }
}
