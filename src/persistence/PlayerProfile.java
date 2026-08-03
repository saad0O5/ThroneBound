package persistence;

/**
 * PlayerProfile
 * Owner: Member C (GUI & Persistence)
 *
 * Responsibility:
 *   - Represents one player's saved account: credentials, unlocked cards,
 *     saved decks, and a reference to their MatchHistory.
 *   - Persisted to/from JSON via ProfileManager.
 *
 * TODO:
 *   - [ ] Fields: username (String), passwordHash (String — never store plaintext),
 *         unlockedCards (List<String>), savedDecks (List<Deck>), matchHistory
 *   - [ ] Decide on the JSON structure for this class (this is the schema
 *         ProfileManager reads/writes)
 */
public class PlayerProfile {
    // TODO: fields
}
