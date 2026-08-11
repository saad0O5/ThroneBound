package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {
    private final String storageDirectory;
    private final Gson gson;

    public ProfileManager(String storageDirectory) {
        this.storageDirectory = storageDirectory;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
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
            Path target = profilePath(profile.getUsername());
            Path tmp = Path.of(target.toString() + ".tmp");
            Files.writeString(tmp, gson.toJson(toDto(profile)), StandardCharsets.UTF_8);
            Files.move(tmp, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save profile for " + profile.getUsername(), e);
        }
    }

    private PlayerProfile readProfile(String username) {
        Path file = profilePath(username);
        if (!Files.exists(file)) return null;
        try {
            String json = Files.readString(file, StandardCharsets.UTF_8);
            return fromDto(gson.fromJson(json, ProfileDto.class));
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

    private ProfileDto toDto(PlayerProfile profile) {
        ProfileDto dto = new ProfileDto();
        dto.username = profile.getUsername();
        dto.passwordHash = profile.getPasswordHash();
        dto.unlockedCards = new ArrayList<>(profile.getUnlockedCards());
        dto.matchHistory = new ArrayList<>(profile.getMatchHistory().getRecords());
        dto.savedDecks = new ArrayList<>();
        for (cards.Deck deck : profile.getSavedDecks()) {
            dto.savedDecks.add(new ArrayList<>(deck.getCardNames()));
        }
        dto.version = 1;
        return dto;
    }

    private PlayerProfile fromDto(ProfileDto dto) {
        PlayerProfile profile = new PlayerProfile(dto.username, dto.passwordHash);
        if (dto.unlockedCards != null) {
            profile.getUnlockedCards().addAll(dto.unlockedCards);
        }
        if (dto.matchHistory != null) {
            for (MatchRecord record : dto.matchHistory) {
                profile.getMatchHistory().addRecord(record);
            }
        }
        if (dto.savedDecks != null) {
            for (List<String> deckNames : dto.savedDecks) {
                profile.saveDeck(cards.Deck.fromCardNames(deckNames));
            }
        }
        return profile;
    }

    private static class ProfileDto {
        String username;
        String passwordHash;
        List<String> unlockedCards;
        List<MatchRecord> matchHistory;
        List<List<String>> savedDecks;
        int version;
    }
}
