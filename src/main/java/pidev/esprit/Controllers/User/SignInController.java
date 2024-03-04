package pidev.esprit.Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pidev.esprit.User.Entities.Role;
import pidev.esprit.User.Entities.User;
import pidev.esprit.User.Services.GestionUser;
import pidev.esprit.User.Services.UserAuthenticationService;
import pidev.esprit.User.Tools.NavigationUtils;

import pidev.esprit.User.Tools.PropertyReader;

import java.io.IOException;

public class SignInController {

    private static final Logger logger = LogManager.getLogger(SignInController.class);

    @FXML
    private TextField emailField;
    @FXML
    private TextField recoveryCodeField;

    @FXML
    private PasswordField newPasswordField;


    @FXML
    private PasswordField passwordField;

    private GestionUser gestionUser;
    private UserAuthenticationService authService;
    private NavigationUtils navigationUtils;

    public SignInController() {
        gestionUser = new GestionUser();
        authService = new UserAuthenticationService(gestionUser);
        navigationUtils = new NavigationUtils();
    }

    @FXML
    void signIn(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (!validateInput(email, password)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid email and password.");
            return;
        }

        User user = gestionUser.SignIn(email, password);
        if (user != null) {
            // Successful sign-in
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue!");
            navigateToDashboard(user, event);
        } else {
            // Invalid credentials
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Email ou mot de passe incorrect");
        }
    }

    @FXML
    void forgotPassword(ActionEvent event) {
        String email = emailField.getText();

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        try {
            authService.initiatePasswordRecovery(email);
            showAlert(Alert.AlertType.INFORMATION, "Password Recovery", "Instructions have been sent to your email address.");

            // Open the password recovery page
            openPasswordRecoveryPage();
        } catch (Exception e) {
            logger.error("Failed to initiate password recovery", e);
            showAlert(Alert.AlertType.ERROR, "Email Error", "Failed to initiate password recovery. Please try again later.");
        }
    }

    private void openPasswordRecoveryPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PasswordRecovery.fxml"));
            Parent recoveryParent = loader.load();
            Scene recoveryScene = new Scene(recoveryParent);

            // Create a new stage for the password recovery page
            Stage recoveryStage = new Stage();
            recoveryStage.setScene(recoveryScene);
            recoveryStage.show();
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
    }


    private boolean validateInput(String email, String password) {
        return isValidEmail(email) && !password.isEmpty();
    }

    private boolean isValidEmail(String email) {
        // Implement your email validation logic here
        return email.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
    }

    private void navigateToDashboard(User user, ActionEvent event) {
        try {
            FXMLLoader loader;
            if (user.getRole_user().equals(Role.ADMIN.getRole())) {
                loader = new FXMLLoader(getClass().getResource("/DashboardAdmin.fxml"));
            } else if (user.getRole_user().equals(Role.CLIENT.getRole())) {
                loader = new FXMLLoader(getClass().getResource("/DashboardClient.fxml"));
            } else {
                return;
            }

            Parent dashboardParent = loader.load();
            Scene dashboardScene = new Scene(dashboardParent);

            navigationUtils.navigateToScene(event, dashboardScene);
        } catch (IOException e) {
            logger.error("Failed to navigate to the dashboard", e);
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate to the dashboard.");
        }
    }

    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
            Parent signUpParent = loader.load();
            Scene signUpScene = new Scene(signUpParent);

            navigationUtils.navigateToScene(event, signUpScene);
        } catch (IOException e) {
            logger.error("Failed to navigate to the sign-up page", e);
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate to the sign-up page.");
        }
    }
    @FXML
    void resetPassword(ActionEvent event) {
        String email = emailField.getText();
        String recoveryCode = recoveryCodeField.getText(); // Assuming you have a field for recovery code
        String newPassword = newPasswordField.getText(); // Assuming you have a field for the new password

        if (!isValidEmail(email) || recoveryCode.isEmpty() || newPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid email, recovery code, and new password.");
            return;
        }

        boolean passwordResetSuccessful = authService.resetPassword(email, recoveryCode, newPassword);

        if (passwordResetSuccessful) {
            showAlert(Alert.AlertType.INFORMATION, "Password Reset", "Your password has been successfully reset.");
            // Additional logic as needed, e.g., navigate to login screen
        } else {
            showAlert(Alert.AlertType.ERROR, "Password Reset Failed", "Invalid recovery code or email.");
        }
    }
}