package cards;

import java.util.ArrayList;
import java.util.List;

/**
 * UndeadLegionFaction
 * Owner: Member B
 */
public class UndeadLegionFaction extends Faction {
    @Override
    protected List<Card> buildCardPool() {
        List<Card> pool = new ArrayList<>();
        pool.add(new SkeletonGrunt());
        pool.add(new BoneServant());
        pool.add(new ZombieShambler());
        pool.add(new Wraith());
        pool.add(new CursedPriest());
        pool.add(new GhoulPack());
        pool.add(new BoneGolem());
        pool.add(new DeathKnight());
        pool.add(new PlagueBearer());
        pool.add(new Necromancer());
        pool.add(new SoulReaper());
        pool.add(new LichAcolyte());
        pool.add(new BoneDragon());
        pool.add(new DeathLordMalachar());
        pool.add(new TheReckoning());
        return pool;
    }
}
