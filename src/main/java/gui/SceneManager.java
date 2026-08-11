package gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Singleton manager for swapping screen roots inside a single application Scene.
 * This avoids creating and destroying multiple Scene/Stage objects and improves
 * desktop UX by preserving window size, position, and styles during navigation.
 */
public final class SceneManager {
    private static SceneManager instance;
    private Stage stage;
    private Scene scene;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void initialize(Stage stage, Scene scene) {
        if (this.stage != null) {
            return;
        }
        this.stage = stage;
        this.scene = scene;
    }

    public boolean isInitialized() {
        return stage != null && scene != null;
    }

    public void setRoot(Parent root) {
        if (scene == null) {
            throw new IllegalStateException("SceneManager has not been initialized");
        }
        // Ensure the new root has the generic 'root' style class so
        // the global `.root` CSS rules (app background/theme) apply.
        if (!root.getStyleClass().contains("root")) {
            root.getStyleClass().add("root");
        }
        scene.setRoot(root);
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }
}
