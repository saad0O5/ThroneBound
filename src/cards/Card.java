package cards;

/**
 * Card (abstract base class)
 * Owner: Member B (Game Engine & Cards/Factions)
 *
 * Responsibility:
 *   - Base type for every card in the game. Holds shared stats and defines the
 *     play() hook that faction-specific subclasses override with their effects.
 *
 * TODO:
 *   - [ ] Fields: name (String), cost (Essence/Mana/Soul combination), attack (int),
 *         health (int)
 *   - [ ] Declare/implement play(GameState state): base behavior is "enter the
 *         board" — subclasses override to add their on-play effect
 *   - [ ] Consider an onDeath(GameState state) hook too, since several Undead
 *         cards (see Card_List.md) trigger effects when they die
 */
public abstract class Card {
    // TODO: fields - name, cost, attack, health

    // TODO: public void play(/* GameState state */)
}
