package cards;

import engine.GameState;

class WolfPup extends BeastkinCard {
    WolfPup() { super("Wolf Pup", new Cost(1, 0, 0), 1, 1); }
    @Override public void play(GameState state) { }
}

class ScoutHawk extends BeastkinCard {
    ScoutHawk() { super("Scout Hawk", new Cost(1, 0, 0), 1, 2); }
    @Override public void play(GameState state) { }
}

class ThornbackBoar extends BeastkinCard {
    ThornbackBoar() { super("Thornback Boar", new Cost(2, 0, 0), 3, 2); }
    @Override public void play(GameState state) { }
}

class ElementalSprite extends BeastkinCard {
    ElementalSprite() { super("Elemental Sprite", new Cost(1, 1, 0), 1, 3); }
    @Override public void play(GameState state) { }
}

class GreyWolf extends BeastkinCard {
    GreyWolf() { super("Grey Wolf", new Cost(2, 0, 0), 2, 2); }
    @Override public void play(GameState state) { }
}

class VineCrawler extends BeastkinCard {
    VineCrawler() { super("Vine Crawler", new Cost(2, 0, 0), 2, 3); }
    @Override public void play(GameState state) { }
}

class BearCub extends BeastkinCard {
    BearCub() { super("Bear Cub", new Cost(2, 0, 0), 3, 3); }
    @Override public void play(GameState state) { }
}

class FeralPanther extends BeastkinCard {
    FeralPanther() { super("Feral Panther", new Cost(3, 0, 0), 4, 2); }
    @Override public void play(GameState state) { }
}

class StoneSapling extends BeastkinCard {
    StoneSapling() { super("Stone Sapling", new Cost(2, 1, 0), 2, 5); }
    @Override public void play(GameState state) { }
}

class AlphaWolf extends BeastkinCard {
    AlphaWolf() { super("Alpha Wolf", new Cost(3, 0, 0), 4, 4); }
    @Override public void play(GameState state) { }
}

class TreantGuardian extends BeastkinCard {
    TreantGuardian() { super("Treant Guardian", new Cost(3, 1, 0), 3, 6); }
    @Override public void play(GameState state) { }
}

class GriffonRider extends BeastkinCard {
    GriffonRider() { super("Griffon Rider", new Cost(3, 0, 1), 5, 4); }
    @Override public void play(GameState state) { }
}

class ElderBear extends BeastkinCard {
    ElderBear() { super("Elder Bear", new Cost(4, 0, 0), 6, 5); }
    @Override public void play(GameState state) { }
}

class BeastmasterKira extends BeastkinCard {
    BeastmasterKira() { super("Beastmaster Kira", new Cost(4, 0, 1), 4, 6); }
    @Override public void play(GameState state) { }
}

class WorldTreeAvatar extends BeastkinCard {
    WorldTreeAvatar() { super("World Tree Avatar", new Cost(5, 0, 2), 7, 8); }
    @Override public void play(GameState state) { }
}
