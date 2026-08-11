package cards;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * CardFactory creates fresh card instances by name.
 */
public final class CardFactory {
    private CardFactory() { }

    public static Card createCard(String cardName) {
        List<Faction> factions = List.of(
                new BeastkinFaction(),
                new ArcaneOrderFaction(),
                new UndeadLegionFaction()
        );
        for (Faction faction : factions) {
            for (Card card : faction.getCardPool()) {
                if (card.getName().equals(cardName)) {
                    return copyCard(card);
                }
            }
        }
        throw new IllegalArgumentException("Unknown card name: " + cardName);
    }

    private static Card copyCard(Card original) {
        try {
            Constructor<? extends Card> constructor = original.getClass().getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to copy card: " + original.getName(), e);
        }
    }
}
