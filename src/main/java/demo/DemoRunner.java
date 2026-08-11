package demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.GameState;
import engine.Player;
import network.GameStateSnapshot;
import cards.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * DemoRunner creates a few canned GameState snapshots for presentation/demo.
 */
public class DemoRunner {
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Scenario 1: Lethal on player 2
        GameState s1 = new GameState();
        s1.dealDamageToPlayer(Player.PLAYER2, 25);
        GameStateSnapshot snap1 = GameStateSnapshot.fromGameState(s1);
        writeJson(gson, snap1, "demo_lethal.json");

        // Scenario 2: Board trade — both players have creatures in lane 0
        GameState s2 = new GameState();
        cards.Card c1 = cards.CardFactory.createCard("Alpha Wolf");
        c1.setOwner(Player.PLAYER1);
        s2.setLaneCard(Player.PLAYER1, 0, c1);
        cards.Card c2 = cards.CardFactory.createCard("Wolf Pup");
        c2.setOwner(Player.PLAYER2);
        s2.setLaneCard(Player.PLAYER2, 0, c2);
        GameStateSnapshot snap2 = GameStateSnapshot.fromGameState(s2);
        writeJson(gson, snap2, "demo_boardtrade.json");

        // Scenario 3: Resource discount example
        GameState s3 = new GameState();
        s3.getResourcesP1().setEssence(5);
        s3.getResourcesP1().setMana(5);
        GameStateSnapshot snap3 = GameStateSnapshot.fromGameState(s3);
        writeJson(gson, snap3, "demo_resources.json");

        System.out.println("Wrote demo snapshots to ./demos/");
    }

    private static void writeJson(Gson gson, GameStateSnapshot snap, String fileName) throws IOException {
        Path dir = Path.of("demos");
        Files.createDirectories(dir);
        String json = gson.toJson(snap);
        Files.writeString(dir.resolve(fileName), json);
    }
}
