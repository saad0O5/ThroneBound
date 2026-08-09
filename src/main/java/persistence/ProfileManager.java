package persistence;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ProfileManager {
    private final String storageDirectory;

    public ProfileManager(String storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    public String getStorageDirectory() { return storageDirectory; }

    public PlayerProfile login(String username, String password) {
        PlayerProfile stored = readProfile(username);
        if (stored == null) {
            throw new InvalidCredentialsException("No such user: " + username);
        }
        if (!hashPassword(password).equals(stored.getPasswordHash())) {
            throw new InvalidCredentialsException("Incorrect password for user: " + username);
        }
        return stored;
    }

    public PlayerProfile register(String username, String password) {
        if (readProfile(username) != null) {
            throw new IllegalStateException("Username already taken: " + username);
        }
        PlayerProfile profile = new PlayerProfile(username, hashPassword(password));
        save(profile);
        return profile;
    }

    public void save(PlayerProfile profile) {
        try {
            Files.createDirectories(Path.of(storageDirectory));
            Files.writeString(profilePath(profile.getUsername()), toJson(profile), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save profile for " + profile.getUsername(), e);
        }
    }

    private PlayerProfile readProfile(String username) {
        Path file = profilePath(username);
        if (!Files.exists(file)) return null;
        try {
            return fromJson(Files.readString(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read profile for " + username, e);
        }
    }

    private Path profilePath(String username) {
        return Path.of(storageDirectory, username + ".json");
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("SHA-256 not available on this JVM", e);
        }
    }

    // ---- Hand-written JSON (no external libraries) ----

    private static String toJson(PlayerProfile profile) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"username\":\"").append(JsonUtil.escape(profile.getUsername())).append("\",");
        sb.append("\"passwordHash\":\"").append(JsonUtil.escape(profile.getPasswordHash())).append("\",");
        sb.append("\"unlockedCards\":[");
        List<String> cards = profile.getUnlockedCards();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(JsonUtil.escape(cards.get(i))).append("\"");
        }
        sb.append("]}");
        return sb.toString();
    }

    private static PlayerProfile fromJson(String json) {
        String username = JsonUtil.unescape(JsonUtil.extractRawField(json, "username"));
        String passwordHash = JsonUtil.unescape(JsonUtil.extractRawField(json, "passwordHash"));
        PlayerProfile profile = new PlayerProfile(username, passwordHash);
        for (String card : JsonUtil.parseStringArray(JsonUtil.extractRawField(json, "unlockedCards"))) {
            profile.addUnlockedCard(card);
        }
        return profile;
    }
}
