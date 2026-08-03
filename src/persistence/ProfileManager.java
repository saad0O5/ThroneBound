package persistence;

/**
 * ProfileManager
 * Owner: Member C (GUI & Persistence)
 *
 * Responsibility:
 *   - Handles login/register against stored player profiles, and saving profile
 *     updates (new unlocks, updated match history, etc.) back to disk as JSON.
 *
 * TODO:
 *   - [ ] Choose a JSON library (e.g. Gson or org.json) and add it to the project
 *   - [ ] Implement register(username, password): create a new PlayerProfile,
 *         hash the password (do NOT store plaintext), write to disk
 *   - [ ] Implement login(username, password): read the matching profile file,
 *         verify password hash, return the PlayerProfile
 *   - [ ] Implement save(PlayerProfile): overwrite that profile's JSON file
 *   - [ ] Decide on file layout: one JSON file per player vs one file for all
 *         profiles (one-file-per-player recommended, simpler concurrent access)
 */
public class ProfileManager {
    // TODO: public PlayerProfile login(String username, String password)

    // TODO: public void register(String username, String password)

    // TODO: public void save(PlayerProfile profile)
}
