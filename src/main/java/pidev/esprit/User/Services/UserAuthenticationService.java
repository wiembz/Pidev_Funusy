package pidev.esprit.User.Services;

import pidev.esprit.User.Entities.User;
import pidev.esprit.User.Tools.EmailService;

public class UserAuthenticationService {
    private GestionUser gestionUser;
    private EmailService emailService;

    public boolean resetPassword(String userEmail, String recoveryCode, String newPassword) {
        // You may want to add additional validation for userEmail, recoveryCode, and newPassword

        // Call the resetPassword method from GestionUser
        return gestionUser.resetPassword(userEmail, recoveryCode, newPassword);
    }

    public UserAuthenticationService(GestionUser gestionUser) {
        this.gestionUser = gestionUser;
        this.emailService = new EmailService(); // Or inject the EmailService instance
    }

    public void initiatePasswordRecovery(String email) throws Exception {
        User user = gestionUser.getUserByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email: " + email);
        }

        int recoveryCode = generateRecoveryCode();
        emailService.sendPasswordRecoveryEmail(email, recoveryCode);
        storeRecoveryCodeForUser(user, recoveryCode);
    }

    private int generateRecoveryCode() {
        // Implement your logic to generate a recovery code
        // For example, you can use a random number generator
        return (int) (Math.random() * 900000) + 100000; // Generates a random 6-digit code
    }

    private void storeRecoveryCodeForUser(User user, int recoveryCode) {
        // Implement the logic to store the recovery code for the user
        // You can use the user's email or id as an identifier and store the recovery code in the database
        // For example:
        gestionUser.storeRecoveryCode(user.getEmail_user(), String.valueOf(recoveryCode));
    }

    // Add other methods as needed, such as verifying the recovery code and resetting the password
}