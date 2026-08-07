package cards;

import java.util.ArrayList;
import java.util.List;

/**
 * BeastkinFaction
 * Owner: Member B
 */
public class BeastkinFaction extends Faction {
    @Override
    protected List<Card> buildCardPool() {
        List<Card> pool = new ArrayList<>();
        pool.add(new WolfPup());
        pool.add(new ScoutHawk());
        pool.add(new ThornbackBoar());
        pool.add(new ElementalSprite());
        pool.add(new GreyWolf());
        pool.add(new VineCrawler());
        pool.add(new BearCub());
        pool.add(new FeralPanther());
        pool.add(new StoneSapling());
        pool.add(new AlphaWolf());
        pool.add(new TreantGuardian());
        pool.add(new GriffonRider());
        pool.add(new ElderBear());
        pool.add(new BeastmasterKira());
        pool.add(new WorldTreeAvatar());
        return pool;
    }
}
