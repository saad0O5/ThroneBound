package persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: ProfileManager.login/register/save, backed by
 * JSON files on disk (locked-in persistence format).
 *
 * Uses a JUnit @TempDir so tests never touch real player data and clean up
 * automatically. RED until register()/login()/save() are implemented.
 */
class ProfileManagerTest {

    @Test
    void registerCreatesARetrievableProfile(@TempDir Path tempDir) {
        ProfileManager manager = new ProfileManager(tempDir.toString());
        PlayerProfile registered = manager.register("hero123", "correct-password");
        assertNotNull(registered);
        assertEquals("hero123", registered.getUsername());
    }

    @Test
    void loginReturnsTheSameProfileAfterRegister(@TempDir Path tempDir) {
        ProfileManager manager = new ProfileManager(tempDir.toString());
        manager.register("hero123", "correct-password");

        PlayerProfile loggedIn = manager.login("hero123", "correct-password");

        assertNotNull(loggedIn);
        assertEquals("hero123", loggedIn.getUsername());
    }

    @Test
    void loginFailsWithWrongPassword(@TempDir Path tempDir) {
        ProfileManager manager = new ProfileManager(tempDir.toString());
        manager.register("hero123", "correct-password");

        // Specifically InvalidCredentialsException (not just any RuntimeException) —
        // this keeps the test properly RED while login() only throws
        // UnsupportedOperationException, and only turns GREEN once login()
        // is actually implemented to reject bad credentials correctly.
        assertThrows(InvalidCredentialsException.class, () ->
            manager.login("hero123", "wrong-password")
        );
    }

    @Test
    void savePersistsUpdatedUnlockedCards(@TempDir Path tempDir) {
        ProfileManager manager = new ProfileManager(tempDir.toString());
        PlayerProfile profile = manager.register("hero123", "correct-password");
        profile.addUnlockedCard("Alpha Wolf");

        manager.save(profile);
        PlayerProfile reloaded = manager.login("hero123", "correct-password");

        assertTrue(reloaded.getUnlockedCards().contains("Alpha Wolf"));
    }
}
