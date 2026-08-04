package persistence;

/**
 * InvalidCredentialsException
 * Owner: Member C (GUI & Persistence)
 *
 * Thrown by ProfileManager.login() when the username doesn't exist or the
 * password doesn't match. Fully implemented — this is just an exception type,
 * no logic to fill in.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
