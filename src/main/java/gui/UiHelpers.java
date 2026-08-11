package gui;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
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

    public static void applyHandCardEffect(Node cardNode, HBox handRow) {
        TranslateTransition lift = new TranslateTransition(Duration.millis(120), cardNode);
        lift.setToY(-22);
        TranslateTransition drop = new TranslateTransition(Duration.millis(120), cardNode);
        drop.setToY(0);

        cardNode.setOnMouseEntered(e -> {
            lift.playFromStart();
            if (handRow != null) handRow.setSpacing(10); // relax overlap
        });
        cardNode.setOnMouseExited(e -> {
            drop.playFromStart();
            if (handRow != null) handRow.setSpacing(-40); // restore overlap
        });
    }
}
