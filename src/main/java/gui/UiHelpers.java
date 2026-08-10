package gui;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public final class UiHelpers {
    private UiHelpers() {
    }

    public static void applyHoverEffect(Button button) {
        ScaleTransition hoverIn = new ScaleTransition(Duration.millis(120), button);
        hoverIn.setToX(1.02);
        hoverIn.setToY(1.02);

        ScaleTransition hoverOut = new ScaleTransition(Duration.millis(120), button);
        hoverOut.setToX(1.0);
        hoverOut.setToY(1.0);

        button.setOnMouseEntered(event -> hoverIn.playFromStart());
        button.setOnMouseExited(event -> hoverOut.playFromStart());
    }
}
