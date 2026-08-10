package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import persistence.InvalidCredentialsException;
import persistence.PlayerProfile;
import persistence.ProfileManager;

public class LoginScreen extends VBox {
    private final ThroneBoundApp app;
    private final ProfileManager profileManager;
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label statusLabel = new Label();

    public LoginScreen(ThroneBoundApp app, ProfileManager profileManager) {
        this.app = app;
        this.profileManager = profileManager;

        setAlignment(Pos.CENTER);
        setSpacing(12);
        setPadding(new Insets(24));

        Label title = new Label("Thronebound Login");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        loginButton.setOnAction(event -> handleLogin());
        registerButton.setOnAction(event -> handleRegister());

        statusLabel.setWrapText(true);
        statusLabel.setStyle("-fx-text-fill: #b22222;");

        getChildren().addAll(
                title,
                usernameField,
                passwordField,
                new VBox(6, loginButton, registerButton, statusLabel)
        );
    }

    private void handleLogin() {
        try {
            PlayerProfile profile = profileManager.login(usernameField.getText(), passwordField.getText());
            app.showMainMenu(profile);
        } catch (InvalidCredentialsException ex) {
            statusLabel.setText(ex.getMessage());
        }
    }

    private void handleRegister() {
        try {
            PlayerProfile profile = profileManager.register(usernameField.getText(), passwordField.getText());
            statusLabel.setText("Registration successful. Please log in.");
            app.showMainMenu(profile);
        } catch (IllegalStateException ex) {
            statusLabel.setText(ex.getMessage());
        }
    }
}
