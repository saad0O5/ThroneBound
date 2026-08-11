package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import persistence.InvalidCredentialsException;
import persistence.PlayerProfile;
import persistence.ProfileManager;

public class LoginScreen extends AnchorPane {
    private final ThroneBoundApp app;
    private final ProfileManager profileManager;
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label statusLabel = new Label();

    public LoginScreen(ThroneBoundApp app, ProfileManager profileManager) {
        this.app = app;
        this.profileManager = profileManager;

        getStyleClass().add("screen-root");

        Label title = new Label("Thronebound");
        title.getStyleClass().add("title-label");
        AnchorPane.setTopAnchor(title, 80.0); // ~top third for 800px -> ~266, use 80 for visual center
        AnchorPane.setLeftAnchor(title, 0.0);
        AnchorPane.setRightAnchor(title, 0.0);
        title.setAlignment(Pos.CENTER);

        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(520);
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(520);

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        loginButton.getStyleClass().add("action-button");
        registerButton.getStyleClass().add("secondary-button");

        loginButton.setOnAction(event -> handleLogin());
        registerButton.setOnAction(event -> handleRegister());

        statusLabel.setWrapText(true);
        statusLabel.getStyleClass().add("status-label");

        VBox formBox = new VBox(14, usernameField, passwordField, statusLabel, loginButton, registerButton);
        formBox.setAlignment(Pos.CENTER);
        formBox.getStyleClass().addAll("panel", "login-panel");
        formBox.setMaxWidth(520);
        formBox.setMinWidth(520);

        AnchorPane.setTopAnchor(formBox, 220.0);
        AnchorPane.setLeftAnchor(formBox, 320.0);
        AnchorPane.setRightAnchor(formBox, 320.0);
        getChildren().addAll(title, formBox);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password cannot be blank.");
            return;
        }

        try {
            PlayerProfile profile = profileManager.login(username, password);
            app.showMainMenu(profile);
        } catch (InvalidCredentialsException ex) {
            statusLabel.setText(ex.getMessage());
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password cannot be blank.");
            return;
        }

        try {
            PlayerProfile profile = profileManager.register(username, password);
            statusLabel.setText("Registration successful. Welcome, " + profile.getUsername() + "!");
            app.showMainMenu(profile);
        } catch (IllegalStateException ex) {
            statusLabel.setText(ex.getMessage());
        }
    }
}
