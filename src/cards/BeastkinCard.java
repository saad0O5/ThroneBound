package cards;

/**
 * BeastkinCard
 * Owner: Member B
 * Extends: Card
 *
 * Responsibility:
 *   - Base class for all Beastkin Clans cards. Implements the shared "Pack"
 *     synergy pattern (bonus stats when other Beastkin are on the board) that
 *     individual Beastkin cards customize.
 *   - See docs/Card_List.md for the full 15-card Beastkin roster and each
 *     card's specific Pack condition/bonus.
 *
 * TODO:
 *   - [ ] Override play() to apply this card's specific Pack effect
 *   - [ ] Consider a helper method (e.g. countOtherBeastkinOnBoard()) shared
 *         across Beastkin cards to avoid duplicating board-scanning logic
 *   - [ ] Implement the 15 individual Beastkin cards from Card_List.md (either
 *         as one configurable class + data, or one subclass each — your choice,
 *         document the decision in ProjectReport.pdf)
 */
public class BeastkinCard /* extends Card */ {
    // TODO
}
