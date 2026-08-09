package persistence;

import cards.ArcaneOrderFaction;
import cards.BeastkinFaction;
import cards.Card;
import cards.Faction;
import cards.UndeadLegionFaction;

import java.util.ArrayList;
import java.util.List;

/**
 * UnlockManager
 * Owner: Member C (GUI & Persistence)
 *
 * PLACEHOLDER RULE — the file's own TODO says to "discuss with team and
 * document the chosen rule." Current rule: each win unlocks the next
 * not-yet-unlocked card, in a fixed order across all three factions.
 * Confirm this with your team before relying on it for grading/demo.
 */
public class UnlockManager {

    private static List<String> masterCardOrder() {
        List<String> names = new ArrayList<>();
        List<Faction> factions = List.of(new BeastkinFaction(), new ArcaneOrderFaction(), new UndeadLegionFaction());
        for (Faction faction : factions) {
            for (Card card : faction.getCardPool()) {
                names.add(card.getName());
            }
        }
        return names;
    }

    public void checkUnlocks(PlayerProfile profile, boolean won) {
        if (!won) return;
        for (String cardName : masterCardOrder()) {
            if (!profile.getUnlockedCards().contains(cardName)) {
                profile.addUnlockedCard(cardName);
                return;
            }
        }
    }
}
