package pidev.esprit.Controllers.User;
import pidev.esprit.User.Entities.Role;
import pidev.esprit.User.Entities.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class UserController {
    private static final Map<String, User> userDatabase = new HashMap<>();




    public static boolean authenticateUser(String username, String password) {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            return user.getMdp().equals(hashPassword(password));
        }
        return false;
    }

    public static boolean isUserAdmin(String username) {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            return Role.ADMIN.getRole().equals(user.getRole_user());
        }
        return false;
    }

    public static boolean registerUser(String nom_user, String prenom_user, String email_user, String mdp, double salaire, LocalDate date_naissance, int CIN, int tel, String adresse_user, String role_user) {

        if (!userDatabase.containsKey(email_user)) {
            if (isStrongPassword(mdp)) {
                String hashedPassword = hashPassword(mdp);


                userDatabase.put(email_user, new User());
                return true;
            } else {
                return false; // Password does not meet the required strength
            }
        }
        return false; // Email already exists
    }


    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private static boolean isStrongPassword(String password) {
        // Add your password strength conditions here
        // For example, requiring a minimum length and the presence of both letters and numbers
        return password.length() >= 8 && password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*");
    }
}
