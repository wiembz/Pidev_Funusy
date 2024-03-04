package pidev.esprit.Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pidev.esprit.User.Services.GestionUser;
import pidev.esprit.User.Services.UserAuthenticationService;

public class PasswordRecoveryController {

    @FXML
    private TextField recoveryCodeField;

    @FXML
    private PasswordField newPasswordField;

    private GestionUser gestionUser;
    private UserAuthenticationService authService;

    public PasswordRecoveryController() {
        gestionUser = new GestionUser();
        authService = new UserAuthenticationService(gestionUser);
    }

    @FXML
    void resetPassword(ActionEvent event) {
        // Retrieve the email from the previous page or pass it as a parameter
        String email = "user@example.com";  // Replace with the actual email retrieval or parameter passing

        String recoveryCode = recoveryCodeField.getText();
        String newPassword = newPasswordField.getText();

        // Validate recovery code and new password
        if (isValidRecoveryCode(recoveryCode) && isValidPassword(newPassword)) {
            // Call the service method to reset the password
            boolean passwordReset = authService.resetPassword(email, recoveryCode, newPassword);

            if (passwordReset) {
                showAlert(Alert.AlertType.INFORMATION, "Password Reset", "Your password has been successfully reset.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Password Reset Failed", "Failed to reset the password. Please try again.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid recovery code and password.");
        }
    }

    private boolean isValidRecoveryCode(String recoveryCode) {
        // Implement recovery code validation logic if needed
        return !recoveryCode.isEmpty();
    }

    private boolean isValidPassword(String newPassword) {
        // Implement password validation logic if needed
        return !newPassword.isEmpty();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
