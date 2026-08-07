package cards;

import engine.GameState;

class ApprenticeMage extends ArcaneCard {
    ApprenticeMage() { super("Apprentice Mage", new Cost(0, 1, 0), 1, 2); }
    @Override public void play(GameState state) { }
}

class ArcaneFamiliar extends ArcaneCard {
    ArcaneFamiliar() { super("Arcane Familiar", new Cost(0, 1, 0), 1, 1); }
    @Override public void play(GameState state) { }
}

class FrostBolt extends ArcaneCard {
    FrostBolt() { super("Frost Bolt", new Cost(0, 1, 0), 0, 0, true); }
    @Override public void play(GameState state) { }
}

class RunicGolem extends ArcaneCard {
    RunicGolem() { super("Runic Golem", new Cost(0, 2, 0), 2, 4); }
    @Override public void play(GameState state) { }
}

class LightningStrike extends ArcaneCard {
    LightningStrike() { super("Lightning Strike", new Cost(0, 2, 0), 0, 0, true); }
    @Override public void play(GameState state) { }
}

class ManaWisp extends ArcaneCard {
    ManaWisp() { super("Mana Wisp", new Cost(1, 2, 0), 1, 3); }
    @Override public void play(GameState state) { }
}

class SorcererAdept extends ArcaneCard {
    SorcererAdept() { super("Sorcerer Adept", new Cost(0, 2, 0), 3, 3); }
    @Override public void play(GameState state) { }
}

class ArcaneShield extends ArcaneCard {
    ArcaneShield() { super("Arcane Shield", new Cost(0, 2, 0), 0, 0, true); }
    @Override public void play(GameState state) { }
}

class BattleMage extends ArcaneCard {
    BattleMage() { super("Battle Mage", new Cost(0, 3, 0), 3, 4); }
    @Override public void play(GameState state) { }
}

class Teleport extends ArcaneCard {
    Teleport() { super("Teleport", new Cost(0, 2, 1), 0, 0, true); }
    @Override public void play(GameState state) { }
}

class ElderWizard extends ArcaneCard {
    ElderWizard() { super("Elder Wizard", new Cost(0, 3, 1), 3, 5); }
    @Override public void play(GameState state) { }
}

class MeteorSwarm extends ArcaneCard {
    MeteorSwarm() { super("Meteor Swarm", new Cost(0, 4, 0), 0, 0, true); }
    @Override public void play(GameState state) { }
}

class StoneWarden extends ArcaneCard {
    StoneWarden() { super("Stone Warden", new Cost(0, 4, 0), 5, 6); }
    @Override public void play(GameState state) { }
}

class ArchmageVeyra extends ArcaneCard {
    ArchmageVeyra() { super("Archmage Veyra", new Cost(0, 4, 1), 4, 6); }
    @Override public void play(GameState state) { }
}

class TimeWarp extends ArcaneCard {
    TimeWarp() { super("Time Warp", new Cost(0, 5, 2), 0, 0, true); }
    @Override public void play(GameState state) { }
}
