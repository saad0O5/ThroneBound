package gui;

/**
 * GameStateObserver (interface)
 * Owner: Member C (GUI)
 *
 * Responsibility:
 *   - Observer-pattern interface so GUI screens (specifically MatchScreen) can
 *     react to GameState changes without the engine module depending on JavaFX.
 *
 * TODO:
 *   - [ ] Declare onStateChanged(GameState state)
 *   - [ ] In MatchScreen's implementation, remember to wrap any UI updates in
 *         Platform.runLater(...) since state changes arrive from a network thread
 */
public interface GameStateObserver {
    // TODO: void onStateChanged(/* GameState state */);
}
