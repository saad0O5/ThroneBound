package cards;

import engine.GameState;
import engine.Lane;
import engine.Player;

class SkeletonGrunt extends UndeadCard {
    SkeletonGrunt() { super("Skeleton Grunt", new Cost(0, 0, 1), 1, 1); }
}

class BoneServant extends UndeadCard {
    BoneServant() { super("Bone Servant", new Cost(0, 0, 1), 2, 1); }
}

class ZombieShambler extends UndeadCard {
    ZombieShambler() { super("Zombie Shambler", new Cost(0, 0, 2), 2, 2); }
}

class Wraith extends UndeadCard {
    Wraith() { super("Wraith", new Cost(0, 0, 2), 1, 3); }
    @Override public void play(GameState state) {
        Player opponent = getOwner() == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        for (Lane lane : state.getLanesForPlayer(opponent)) {
            if (!lane.isEmpty()) {
                lane.getOccupant().modifyAttack(-1);
            }
        }
    }
}

class CursedPriest extends UndeadCard {
    CursedPriest() { super("Cursed Priest", new Cost(1, 0, 2), 2, 3); }
    @Override public void play(GameState state) {
        if (getLane() != null) {
            getLane().getOccupant().modifyHealth(2);
        }
    }
}

class GhoulPack extends UndeadCard {
    GhoulPack() { super("Ghoul Pack", new Cost(0, 0, 2), 3, 2); }
    @Override public void play(GameState state) {
        setAttack(getAttack() + 1);
        setHealth(getHealth() + 1);
    }
}

class BoneGolem extends UndeadCard {
    BoneGolem() { super("Bone Golem", new Cost(0, 0, 3), 3, 5); }
}

class DeathKnight extends UndeadCard {
    DeathKnight() { super("Death Knight", new Cost(0, 0, 3), 4, 3); }
}

class PlagueBearer extends UndeadCard {
    PlagueBearer() { super("Plague Bearer", new Cost(0, 0, 3), 2, 4); }
    @Override public void play(GameState state) {
        Player opponent = getOwner() == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        for (Lane lane : state.getLanesForPlayer(opponent)) {
            if (!lane.isEmpty()) {
                lane.getOccupant().modifyHealth(-1);
            }
        }
    }
}

class Necromancer extends UndeadCard {
    Necromancer() { super("Necromancer", new Cost(1, 0, 3), 2, 4); }
    @Override public void play(GameState state) {
        state.getCurrentEffectState().setNextTurnManaBonus(1);
    }
}

class SoulReaper extends UndeadCard {
    SoulReaper() { super("Soul Reaper", new Cost(0, 0, 4), 4, 4); }
}

class LichAcolyte extends UndeadCard {
    LichAcolyte() { super("Lich Acolyte", new Cost(0, 0, 4), 3, 6); }
    @Override public void onDeath(GameState state) {
        for (Lane lane : state.getLanesForPlayer(getOwner())) {
            if (!lane.isEmpty()) {
                lane.getOccupant().modifyAttack(1);
                lane.getOccupant().modifyHealth(1);
            }
        }
    }
}

class BoneDragon extends UndeadCard {
    BoneDragon() { super("Bone Dragon", new Cost(0, 0, 5), 6, 5); }
}

class DeathLordMalachar extends UndeadCard {
    DeathLordMalachar() { super("Death Lord Malachar", new Cost(1, 0, 5), 5, 7); }
    @Override public void play(GameState state) {
        for (Lane lane : state.getOpponentLanes(getOwner())) {
            if (!lane.isEmpty()) {
                lane.getOccupant().modifyHealth(-2);
            }
        }
    }
}

class TheReckoning extends UndeadCard {
    TheReckoning() { super("The Reckoning", new Cost(0, 0, 6), 0, 0, true); }
    @Override public void play(GameState state) {
        for (Lane lane : state.getOpponentLanes(getOwner())) {
            if (!lane.isEmpty()) {
                lane.getOccupant().modifyHealth(-3);
            }
        }
    }
}
