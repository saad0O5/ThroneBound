# Thronebound

A turn-based, network-multiplayer strategy card duel game built in Java (JavaFX) for CS-212: Object-Oriented Programming (Summer 2026), NUST SEECS.

Two players connect over LAN sockets, build a deck from one of three asymmetric fantasy factions, and battle across a 3-lane board using a custom multi-resource economy — with persistent player profiles, match history, and card unlocks saved between sessions.

## Features

- **Real client-server multiplayer** over Java Sockets — one server thread per connected client
- **Lane-based tactical combat** — 3 lanes per side, mirrored-lane targeting
- **Three asymmetric factions**, each with a distinct mechanical identity:
  - **Beastkin Clans** — cheap, aggressive creatures with "Pack" synergy
  - **Arcane Order** — powerful spells and lane/resource-manipulating effects
  - **Undead Legion** — sacrifice and death-trigger/resurrection loop
- **Custom 3-resource economy** (Essence / Mana / Soul) instead of a single generic resource
- **Two selectable win conditions** — Standard (no turn limit) and Timed (30-turn cap with a tiebreaker)
- **Persistent player profiles** — login/register, win/loss record, unlockable cards, saved decks, and match history, stored in JSON
- **JavaFX GUI** — login, main menu, host/join, deck builder, match screen, and results screen

## Tech Stack

- **Language:** Java
- **GUI:** JavaFX
- **Networking:** Java Sockets (client-server)
- **Concurrency:** Java Threads with synchronized access to shared game state
- **Persistence:** JSON file storage
- **Version Control:** Git / GitHub

## Project Structure

```
thronebound/
├── src/
│   ├── main/java/
│   │   ├── network/       # GameServer, ClientHandler, GameClient, Message protocol
│   │   ├── engine/        # GameState, Lane, TurnManager, CombatResolver, ResourcePool, WinCondition
│   │   ├── cards/         # Card & Faction hierarchy, Deck, Cost
│   │   ├── persistence/   # PlayerProfile, ProfileManager, MatchHistory, UnlockManager
│   │   └── gui/           # JavaFX screens (Login, MainMenu, HostJoin, DeckBuilder, Match, Results)
│   └── test/java/
│       ├── network/       # MessageTest, NetworkIntegrationTest
│       ├── engine/        # GameStateTest, LaneTest, ResourcePoolTest, WinConditionTest, ...
│       ├── cards/         # CardTest, FactionTest, DeckTest, BeastkinCardTest
│       └── persistence/   # PlayerProfileTest, ProfileManagerTest
├── docs/
│   ├── Thronebound_UML_Class_Diagram.pdf
│   ├── Card_List.md
│   └── ProjectReport.pdf
├── pom.xml
├── .gitignore
└── README.md
```

## Team

| Member | Module |
|---|---|
| _Member A_ | Networking & Concurrency |
| _Member B_ | Game Engine & Cards/Factions |
| _Member C_ | GUI & Persistence |

## Testing (TDD Workflow)

This project follows test-driven development: the test suite in `src/test/java/` was written from the UML class diagram **before** the corresponding logic was implemented. Every class's main-source file has TODO-marked methods that currently throw `UnsupportedOperationException` — this is the intentional starting ("red") state.

**Workflow for each class you implement:**
1. Open the relevant test file (e.g. `src/test/java/engine/LaneTest.java` for `Lane`)
2. Run the tests — they'll fail against the `UnsupportedOperationException` stubs
3. Implement the TODO method(s) in the matching main-source file
4. Re-run the tests until they pass ("green")
5. Move to the next class — see `docs/ProjectReport.pdf` (once written) for suggested implementation order

### Running the tests

This project uses Maven and JUnit 5.

```
mvn test
```

If Maven is not available on your path, use your IDE's built-in Maven support or install Maven locally.

Requires JDK 17+ and JavaFX 21. The GUI screens can be verified manually after the engine, networking, and persistence layers are working.

### Running the application

From the command line, use the JavaFX Maven plugin:

```
mvn javafx:run
```

If you prefer a packaged jar, build with:

```
mvn package
```

Then run the jar with an appropriate JavaFX module path or your IDE's JavaFX runtime settings.

### Test coverage by module

| Module | Test file(s) | Notes |
|---|---|---|
| `engine` | `GameStateTest`, `LaneTest`, `ResourcePoolTest`, `WinConditionTest`, `TurnManagerTest`, `CombatResolverTest` | Includes a concurrency test on `GameState` — see `GameStateTest.concurrentPlayCardsDoNotCorruptState()` |
| `cards` | `CardTest`, `FactionTest`, `DeckTest`, `BeastkinCardTest` | `FactionTest` expects each faction's card pool to reach 15 cards once populated |
| `persistence` | `PlayerProfileTest`, `ProfileManagerTest` | Uses JUnit's `@TempDir` so tests never touch real player data |
| `network` | `MessageTest`, `NetworkIntegrationTest` | `NetworkIntegrationTest` is a real localhost client-server test, not a mock — most useful to run manually as you implement `GameServer`/`GameClient` |
| `gui` | _(none — verify manually)_ | |



1. Clone the repository
   ```
   git clone https://github.com/<your-username>/thronebound.git
   ```
2. Open in your IDE of choice (IntelliJ IDEA / Eclipse) as a Java project
3. Ensure JavaFX SDK is configured (see your IDE's JavaFX setup docs)
4. Run `GameServer` on one machine, then `GameClient` on up to one other machine on the same LAN to connect

## Course Context

Built as the final group project for **CS-212: Object-Oriented Programming (Summer 2026)** at NUST SEECS, under the domain **"Multiplayer Strategy Game (Full)."**
