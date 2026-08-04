package persistence;

/**
 * ProfileManager
 * Owner: Member C (GUI & Persistence)
 *
 * TODO:
 *   - [ ] Choose a JSON library (Gson or org.json) and add it to pom.xml
 *   - [ ] Implement register(): create a PlayerProfile, HASH the password
 *         (never store plaintext), write it to disk as JSON
 *   - [ ] Implement login(): read the matching profile file, verify the
 *         password hash, return the PlayerProfile — throw
 *         InvalidCredentialsException if the username doesn't exist or the
 *         password doesn't match
 *   - [ ] Implement save(): overwrite that profile's JSON file
 *   - [ ] See persistence/ProfileManagerTest.java for the expected
 *         register -> login round-trip behavior
 */
public class ProfileManager {
    private final String storageDirectory;

    public ProfileManager(String storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    public String getStorageDirectory() { return storageDirectory; }

    /** TODO: implement. */
    public PlayerProfile login(String username, String password) {
        throw new UnsupportedOperationException("TODO: implement login()");
    }

    /** TODO: implement. */
    public PlayerProfile register(String username, String password) {
        throw new UnsupportedOperationException("TODO: implement register()");
    }

    /** TODO: implement. */
    public void save(PlayerProfile profile) {
        throw new UnsupportedOperationException("TODO: implement save()");
    }
}
