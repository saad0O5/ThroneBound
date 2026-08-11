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
        setSpacing(14);
        setPadding(new Insets(32));
        getStyleClass().add("screen-root");

        Label title = new Label("Thronebound");
        title.getStyleClass().add("title-label");

        Label subtitle = new Label("Enter your credentials to begin your duel");
        subtitle.getStyleClass().add("subtitle-label");

        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(320);
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(320);

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        loginButton.getStyleClass().add("action-button");
        registerButton.getStyleClass().add("secondary-button");

        loginButton.setOnAction(event -> handleLogin());
        registerButton.setOnAction(event -> handleRegister());

        statusLabel.setWrapText(true);
        statusLabel.getStyleClass().add("status-label");

        VBox formBox = new VBox(10, usernameField, passwordField, loginButton, registerButton, statusLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.getStyleClass().add("panel");
        formBox.setMaxWidth(380);
        getChildren().addAll(title, subtitle, formBox);
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
