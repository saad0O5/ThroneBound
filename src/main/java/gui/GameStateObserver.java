package gui;

import engine.GameState;

/**
 * Observer-pattern interface so GUI screens can react to GameState changes
 * without the engine module depending on JavaFX.
 */
public interface GameStateObserver {
    void onStateChanged(GameState state);
}
