package network;

import cards.Card;
import cards.CardFactory;
import engine.GameState;
import engine.Player;
import engine.ResourcePool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Snapshot DTO for sending a full game state over the wire.
 */
public class GameStateSnapshot implements Serializable {
    private int player1Life;
    private int player2Life;
    private int actionsThisTurn;
    private Player currentTurn;
    private ResourceState resourcesP1;
    private ResourceState resourcesP2;
    private PlayerEffectState player1Effects;
    private PlayerEffectState player2Effects;
    private List<CardSnapshot> lanesP1;
    private List<CardSnapshot> lanesP2;
    private List<String> graveyard;
    private List<String> graveyardP1;
    private List<String> graveyardP2;

    public GameStateSnapshot() { }

    public static GameStateSnapshot fromGameState(GameState state) {
        GameStateSnapshot snapshot = new GameStateSnapshot();
        snapshot.player1Life = state.getPlayer1Life();
        snapshot.player2Life = state.getPlayer2Life();
        snapshot.actionsThisTurn = state.getActionsThisTurn();
        snapshot.currentTurn = state.getCurrentTurn();
        snapshot.resourcesP1 = new ResourceState(state.getResourcesP1());
        snapshot.resourcesP2 = new ResourceState(state.getResourcesP2());
        snapshot.player1Effects = new PlayerEffectState(state.getPlayer1EffectState().getBeastkinEssenceDiscount(), state.getPlayer1EffectState().getNextSpellManaDiscount(), state.getPlayer1EffectState().getNextTurnManaBonus(), state.getPlayer1EffectState().isExtraTurnPending());
        snapshot.player2Effects = new PlayerEffectState(state.getPlayer2EffectState().getBeastkinEssenceDiscount(), state.getPlayer2EffectState().getNextSpellManaDiscount(), state.getPlayer2EffectState().getNextTurnManaBonus(), state.getPlayer2EffectState().isExtraTurnPending());
        snapshot.lanesP1 = new ArrayList<>();
        snapshot.lanesP2 = new ArrayList<>();
        for (int i = 0; i < GameState.LANES_PER_PLAYER; i++) {
            snapshot.lanesP1.add(CardSnapshot.fromCard(state.getLanesP1().get(i).getOccupant()));
            snapshot.lanesP2.add(CardSnapshot.fromCard(state.getLanesP2().get(i).getOccupant()));
        }
        snapshot.graveyardP1 = new ArrayList<>();
        snapshot.graveyardP2 = new ArrayList<>();
        for (Card card : state.getGraveyardForPlayer(Player.PLAYER1)) {
            snapshot.graveyardP1.add(card.getName());
        }
        for (Card card : state.getGraveyardForPlayer(Player.PLAYER2)) {
            snapshot.graveyardP2.add(card.getName());
        }
        return snapshot;
    }

    public void applyTo(GameState state) {
        state.setPlayer1Life(player1Life);
        state.setPlayer2Life(player2Life);
        state.setActionsThisTurn(actionsThisTurn);
        state.setCurrentTurn(currentTurn);
        state.getResourcesP1().setEssence(resourcesP1.essence);
        state.getResourcesP1().setMana(resourcesP1.mana);
        state.getResourcesP1().setSoul(resourcesP1.soul);
        state.getResourcesP2().setEssence(resourcesP2.essence);
        state.getResourcesP2().setMana(resourcesP2.mana);
        state.getResourcesP2().setSoul(resourcesP2.soul);
        state.setPlayer1EffectState(player1Effects.toEffectState());
        state.setPlayer2EffectState(player2Effects.toEffectState());
        state.clearBoard();
        for (int i = 0; i < lanesP1.size(); i++) {
            CardSnapshot snapshot = lanesP1.get(i);
            if (snapshot != null) {
                Card card = snapshot.toCard();
                state.setLaneCard(Player.PLAYER1, i, card);
            }
        }
        for (int i = 0; i < lanesP2.size(); i++) {
            CardSnapshot snapshot = lanesP2.get(i);
            if (snapshot != null) {
                Card card = snapshot.toCard();
                state.setLaneCard(Player.PLAYER2, i, card);
            }
        }
        state.getGraveyardForPlayer(Player.PLAYER1).clear();
        state.getGraveyardForPlayer(Player.PLAYER2).clear();
        if (graveyardP1 != null) {
            for (String name : graveyardP1) {
                state.getGraveyardForPlayer(Player.PLAYER1).add(CardFactory.createCard(name));
            }
        }
        if (graveyardP2 != null) {
            for (String name : graveyardP2) {
                state.getGraveyardForPlayer(Player.PLAYER2).add(CardFactory.createCard(name));
            }
        }
    }

    public static class CardSnapshot implements Serializable {
        private String name;
        private int attack;
        private int health;
        private int essenceCost;
        private int manaCost;
        private int soulCost;
        private boolean spell;
        private Player owner;

        public CardSnapshot() { }

        public static CardSnapshot fromCard(Card card) {
            if (card == null) return null;
            CardSnapshot snapshot = new CardSnapshot();
            snapshot.name = card.getName();
            snapshot.attack = card.getAttack();
            snapshot.health = card.getHealth();
            snapshot.essenceCost = card.getCost().getEssence();
            snapshot.manaCost = card.getCost().getMana();
            snapshot.soulCost = card.getCost().getSoul();
            snapshot.spell = card.isSpell();
            snapshot.owner = card.getOwner();
            return snapshot;
        }

        public Card toCard() {
            Card card = CardFactory.createCard(name);
            card.setOwner(owner);
            card.setHealth(health);
            return card;
        }
    }

    public static class ResourceState implements Serializable {
        private int essence;
        private int mana;
        private int soul;

        public ResourceState() { }

        ResourceState(ResourcePool resources) {
            this.essence = resources.getEssence();
            this.mana = resources.getMana();
            this.soul = resources.getSoul();
        }
    }

    public static class PlayerEffectState implements Serializable {
        private int beastkinEssenceDiscount;
        private int nextSpellManaDiscount;
        private int nextTurnManaBonus;
        private boolean extraTurnPending;

        public PlayerEffectState() { }

        PlayerEffectState(int beastkinEssenceDiscount, int nextSpellManaDiscount, int nextTurnManaBonus, boolean extraTurnPending) {
            this.beastkinEssenceDiscount = beastkinEssenceDiscount;
            this.nextSpellManaDiscount = nextSpellManaDiscount;
            this.nextTurnManaBonus = nextTurnManaBonus;
            this.extraTurnPending = extraTurnPending;
        }

        PlayerEffectState(GameState.EffectState state) {
            this.beastkinEssenceDiscount = state.getBeastkinEssenceDiscount();
            this.nextSpellManaDiscount = state.getNextSpellManaDiscount();
            this.nextTurnManaBonus = state.getNextTurnManaBonus();
            this.extraTurnPending = state.isExtraTurnPending();
        }

        GameState.EffectState toEffectState() {
            GameState.EffectState state = new GameState.EffectState();
            state.setBeastkinEssenceDiscount(beastkinEssenceDiscount);
            state.setNextSpellManaDiscount(nextSpellManaDiscount);
            state.setNextTurnManaBonus(nextTurnManaBonus);
            state.setExtraTurnPending(extraTurnPending);
            return state;
        }
    }
}
