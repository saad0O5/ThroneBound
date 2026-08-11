package cards;

import engine.GameState;
import engine.Lane;
import engine.Player;

class ApprenticeMage extends ArcaneCard {
    ApprenticeMage() { super("Apprentice Mage", new Cost(0, 1, 0), 1, 2); }
}

class ArcaneFamiliar extends ArcaneCard {
    ArcaneFamiliar() { super("Arcane Familiar", new Cost(0, 1, 0), 1, 1); }
}

class FrostBolt extends ArcaneCard {
    FrostBolt() { super("Frost Bolt", new Cost(0, 1, 0), 0, 0, true); }
    @Override public void play(GameState state) {
        Player opponent = getOwner() == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        for (Lane lane : state.getLanesForPlayer(opponent)) {
            if (!lane.isEmpty()) {
                lane.getOccupant().modifyHealth(-1);
            }
        }
    }
}

class RunicGolem extends ArcaneCard {
    RunicGolem() { super("Runic Golem", new Cost(0, 2, 0), 2, 4); }
}

class LightningStrike extends ArcaneCard {
    LightningStrike() { super("Lightning Strike", new Cost(0, 2, 0), 0, 0, true); }
    @Override public void play(GameState state) {
        Player opponent = getOwner() == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        for (Lane lane : state.getLanesForPlayer(opponent)) {
            if (!lane.isEmpty()) {
                lane.getOccupant().modifyHealth(-3);
            }
        }
    }
}

class ManaWisp extends ArcaneCard {
    ManaWisp() { super("Mana Wisp", new Cost(1, 2, 0), 1, 3); }
}

class SorcererAdept extends ArcaneCard {
    SorcererAdept() { super("Sorcerer Adept", new Cost(0, 2, 0), 3, 3); }
}

class ArcaneShield extends ArcaneCard {
    ArcaneShield() { super("Arcane Shield", new Cost(0, 2, 0), 0, 0, true); }
    @Override public void play(GameState state) {
        if (getLane() != null && getLane().getOccupant() == this) {
            modifyHealth(3);
        }
    }
}

class BattleMage extends ArcaneCard {
    BattleMage() { super("Battle Mage", new Cost(0, 3, 0), 3, 4); }
}

class Teleport extends ArcaneCard {
    Teleport() { super("Teleport", new Cost(0, 2, 1), 0, 0, true); }
    @Override public void play(GameState state) {
        Player owner = getOwner();
        if (owner == null) return;
        for (Lane lane : state.getLanesForPlayer(owner)) {
            if (!lane.isEmpty() && lane.getOccupant() != this) {
                lane.getOccupant().setLane(null);
            }
        }
    }
}

class ElderWizard extends ArcaneCard {
    ElderWizard() { super("Elder Wizard", new Cost(0, 3, 1), 3, 5); }
}

class MeteorSwarm extends ArcaneCard {
    MeteorSwarm() { super("Meteor Swarm", new Cost(0, 4, 0), 0, 0, true); }
    @Override public void play(GameState state) {
        Player opponent = getOwner() == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        for (Lane lane : state.getLanesForPlayer(opponent)) {
            if (!lane.isEmpty()) {
                lane.getOccupant().modifyHealth(-2);
            }
        }
    }
}

class StoneWarden extends ArcaneCard {
    StoneWarden() { super("Stone Warden", new Cost(0, 4, 0), 5, 6); }
}

class ArchmageVeyra extends ArcaneCard {
    ArchmageVeyra() { super("Archmage Veyra", new Cost(0, 4, 1), 4, 6); }
    @Override public void play(GameState state) {
        state.getCurrentEffectState().setNextSpellManaDiscount(1);
    }
}

class TimeWarp extends ArcaneCard {
    TimeWarp() { super("Time Warp", new Cost(0, 5, 2), 0, 0, true); }
    @Override public void play(GameState state) {
        state.endTurn();
    }
}
