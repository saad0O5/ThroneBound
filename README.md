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
│   ├── network/       # GameServer, ClientHandler, GameClient, Message protocol
│   ├── engine/        # GameState, Lane, TurnManager, CombatResolver, ResourcePool, WinCondition
│   ├── cards/         # Card & Faction hierarchy, Deck
│   ├── persistence/   # PlayerProfile, ProfileManager, MatchHistory, UnlockManager
│   └── gui/            # JavaFX screens (Login, MainMenu, HostJoin, DeckBuilder, Match, Results)
├── docs/
│   ├── Thronebound_UML_Class_Diagram.pdf
│   ├── Card_List.md
│   └── ProjectReport.pdf
├── .gitignore
└── README.md
```

## Team

| Member | Module |
|---|---|
| _Member A_ | Networking & Concurrency |
| _Member B_ | Game Engine & Cards/Factions |
| _Member C_ | GUI & Persistence |

## Getting Started

1. Clone the repository
   ```
   git clone https://github.com/<your-username>/thronebound.git
   ```
2. Open in your IDE of choice (IntelliJ IDEA / Eclipse) as a Java project
3. Ensure JavaFX SDK is configured (see your IDE's JavaFX setup docs)
4. Run `GameServer` on one machine, then `GameClient` on up to one other machine on the same LAN to connect

## Course Context

Built as the final group project for **CS-212: Object-Oriented Programming (Summer 2026)** at NUST SEECS, under the domain **"Multiplayer Strategy Game (Full)."**