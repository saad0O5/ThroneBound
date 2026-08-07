package cards;

import java.util.ArrayList;
import java.util.List;

/**
 * ArcaneOrderFaction
 * Owner: Member B
 */
public class ArcaneOrderFaction extends Faction {
    @Override
    protected List<Card> buildCardPool() {
        List<Card> pool = new ArrayList<>();
        pool.add(new ApprenticeMage());
        pool.add(new ArcaneFamiliar());
        pool.add(new FrostBolt());
        pool.add(new RunicGolem());
        pool.add(new LightningStrike());
        pool.add(new ManaWisp());
        pool.add(new SorcererAdept());
        pool.add(new ArcaneShield());
        pool.add(new BattleMage());
        pool.add(new Teleport());
        pool.add(new ElderWizard());
        pool.add(new MeteorSwarm());
        pool.add(new StoneWarden());
        pool.add(new ArchmageVeyra());
        pool.add(new TimeWarp());
        return pool;
    }
}
