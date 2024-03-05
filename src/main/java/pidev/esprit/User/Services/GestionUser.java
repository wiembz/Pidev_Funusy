package pidev.esprit.User.Services;

import pidev.esprit.User.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import pidev.esprit.Tools.MyConnection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GestionUser implements IGestionUser<User> {
    private Connection cnx2;

    public GestionUser() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void createUser(User user) {
        if (userExists(user)) {
            System.out.println("User already exists");
            return;
        }
        String requet = "INSERT INTO user (nom_user, prenom_user, email_user, mdp, salaire, date_naissance, CIN, tel, adresse_user, role_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = cnx2.prepareStatement(requet);
            pst.setString(1, user.getNom_user());
            pst.setString(2, user.getPrenom_user());
            pst.setString(3, user.getEmail_user());
            pst.setString(4, user.getMdp());
            pst.setDouble(5, user.getSalaire());
            pst.setDate(6, new java.sql.Date(user.getDate_naissance().getTime()));
            pst.setInt(7, user.getCIN());
            pst.setInt(8, user.getTel());
            pst.setString(9, user.getAdresse_user());
            pst.setString(10, user.getRole_user());

            pst.executeUpdate();
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean signIn(String email, String password) {
        String query = "SELECT * FROM user WHERE email_user = ? AND mdp = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            return rs.next(); // If there is a result, the email and password match
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> afficherUser() {
        List<User> Users = new ArrayList<>();
        String req3 = "SELECT * FROM User";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs = stm.executeQuery(req3); // Store Result
            while (rs.next()) {
                try {
                    User p = new User();
                    p.setId_user(rs.getInt(1));
                    p.setNom_user(rs.getString(2));
                    p.setPrenom_user(rs.getString(3));
                    p.setEmail_user(rs.getString(4));
                    p.setMdp(rs.getString(5));
                    p.setSalaire(rs.getDouble(6));
                    p.setDate_naissance(rs.getDate(7));
                    p.setCIN(rs.getInt(8));
                    p.setTel(rs.getInt(9));
                    p.setAdresse_user(rs.getString(10));
                    p.setRole_user(rs.getString(11));
                    Users.add(p);
                } catch (NumberFormatException e) {
                    // Handle the case where the salaire value cannot be parsed as a double
                    System.err.println("Error parsing salaire value: " + e.getMessage());
                    // You may choose to skip this row or set a default value for salaire
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Users;
    }

    @Override
    public void updateUser(User user) {
        String req = "UPDATE user SET nom_user = ?, prenom_user = ?, email_user = ?, mdp = ?, salaire = ?, date_naissance = ?, CIN = ?, tel = ?, adresse_user = ?, role_user = ? WHERE id_user = ?";
        try

        {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setString(1,user.getNom_user());
            pst.setString(2, user.getPrenom_user());
            pst.setString(3, user.getEmail_user());
            pst.setString(4, user.getMdp());
            pst.setDouble(5, user.getSalaire());
            pst.setDate(6, new Date(user.getDate_naissance().getTime()));
            pst.setInt(7, user.getCIN());
            pst.setInt(8, user.getTel());
            pst.setString(9, user.getAdresse_user()); // Assuming you want to store the enum as a string
            pst.setString(10, user.getRole_user()); // Assuming you want to store the enum name
            pst.setInt(11, user.getId_user()); // Set the id_user
            pst.executeUpdate();
            System.out.println("Update successful");
        } catch(
                SQLException e)

        {
            System.err.println("Error updating entity: " + e.getMessage());
        }
    }
    public void updateUserClient(User user) {
        String req = "UPDATE user SET nom_user = ?, prenom_user = ?, email_user = ?, mdp = ?, salaire = ?, CIN = ?, tel = ?, adresse_user = ? = ? WHERE id_user = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, user.getId_user()); // Set the id_user

            pst.setString(2, user.getNom_user());
            pst.setString(3, user.getPrenom_user());
            pst.setString(4, user.getEmail_user());
            pst.setString(5, user.getMdp());
            pst.setDouble(6, user.getSalaire());
            pst.setInt(7, user.getCIN());
            pst.setInt(8, user.getTel());
            pst.setString(9, user.getAdresse_user()); // Assuming you want to store the enum as a string
            // Assuming you want to store the enum name
            pst.executeUpdate();
            System.out.println("Update successful");
        } catch (SQLException e) {
            System.err.println("Error updating entity: " + e.getMessage());
        }
    }




    @Override
    public void deleteUser(int userId) {
        String req = "DELETE FROM user WHERE id_user = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, userId);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
    public User selectUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email_user = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id_user");
                String nom = rs.getString("nom_user");
                String prenom = rs.getString("prenom_user");
                String emailUser = rs.getString("email_user");
                String mdp = rs.getString("mdp");
                double salaire = rs.getDouble("salaire");
                Date dateNaissance = rs.getDate("date_naissance");
                int cin = rs.getInt("CIN");
                int tel = rs.getInt("tel");
                String adresse = rs.getString("adresse_user");
                String role = rs.getString("role_user");

                return new User(id, nom, prenom, emailUser, mdp, salaire, dateNaissance, cin, tel, adresse, role);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null; // Return null if no user found or an error occurs
    }

    public boolean userExists(User p) {
        String query = "SELECT COUNT(*) FROM user WHERE email=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setString(1, p.getEmail_user());

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count > 0, user with the provided email exists
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false; // In case of an exception or other issues, assume user does not exist
    }
    public User SignIn(String email, String password) {
        String query = "SELECT * FROM user WHERE email_user = ? AND mdp = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id_user");
                String nom = rs.getString("nom_user");
                String prenom = rs.getString("prenom_user");
                String emailUser = rs.getString("email_user");
                String mdp = rs.getString("mdp");
                double salaire = rs.getDouble("salaire");
                Date dateNaissance = rs.getDate("date_naissance");
                int cin = rs.getInt("CIN");
                int tel = rs.getInt("tel");
                String adresse = rs.getString("adresse_user");
                String role = rs.getString("role_user");

                return new User(id, nom, prenom, emailUser, mdp, salaire, dateNaissance, cin, tel, adresse, role);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null; // Return null if no user found or an error occurs
    }
//    public void sendForgotPasswordEmail(User user) {
//        String numericCode = generateNumericCode();
//        storeNumericCodeInDatabase(user, numericCode);
//
//        String subject = "Password Reset";
//        String body = "Dear " + user.getNom_user() + ",\n\n"
//                + "To reset your password, please use the following numeric code: " + numericCode;
//
//        // Send the email using JavaMail API
//        // Implement the email sending logic here
//        EmailSender.sendEmail(user.getEmail_user(), subject, body);
//    }

    public boolean resetPasswordWithCode(String email, String numericCode, String newPassword) {
        // Verify the numeric code and check its validity
        if (isNumericCodeValid(email, numericCode)) {
            // Reset the password in the database
            updateUserPassword(email, newPassword);

            // Optionally, invalidate or remove the numeric code from the database
            invalidateNumericCode(email, numericCode);

            return true; // Password reset successful
        }

        return false; // Invalid numeric code or expired
    }

    private String generateNumericCode() {
        // Implement the logic to generate a random numeric code
        // Return the generated code as a string
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6-digit numeric code
    }

    private void storeNumericCodeInDatabase(User user, String numericCode) {
        // Implement the logic to store the numeric code in the database
        // You can use the user's email as an identifier and store the numeric code in the database
        String query = "UPDATE user SET numeric_code = ? WHERE email_user = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setString(1, numericCode);
            pst.setString(2, user.getEmail_user());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean isNumericCodeValid(String email, String numericCode) {
        // Implement the logic to check if the numeric code is valid for the given email
        // Verify the code against the stored code in the database and check its expiration

        String query = "SELECT numeric_code FROM user WHERE email_user = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String storedCode = rs.getString("numeric_code");
                return storedCode != null && storedCode.equals(numericCode); // Check if the stored code matches the provided code
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    private void updateUserPassword(String email, String newPassword) {
        // Implement the logic to update the user's password in the database
        String query = "UPDATE user SET mdp = ? WHERE email_user = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setString(1, newPassword);
            pst.setString(2, email);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    private void invalidateNumericCode(String email, String numericCode) {
        // Optionally, implement the logic to invalidate or remove the numeric code from the database
        String query = "UPDATE user SET numeric_code = NULL WHERE email_user = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setString(1, email);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email_user = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id_user");
                String nom = rs.getString("nom_user");
                String prenom = rs.getString("prenom_user");
                String emailUser = rs.getString("email_user");
                String mdp = rs.getString("mdp");
                double salaire = rs.getDouble("salaire");
                Date dateNaissance = rs.getDate("date_naissance");
                int cin = rs.getInt("CIN");
                int tel = rs.getInt("tel");
                String adresse = rs.getString("adresse_user");
                String role = rs.getString("role_user");

                return new User(id, nom, prenom, emailUser, mdp, salaire, dateNaissance, cin, tel, adresse, role);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null; // Return null if no user found or an error occurs
    }
    public void storeRecoveryCode(String userEmail, String recoveryCode) {
        String query = "UPDATE user SET numeric_code = ? WHERE email_user = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setString(1, recoveryCode);
            pst.setString(2, userEmail);
            pst.executeUpdate();
            System.out.println("Recovery code stored for user with email: " + userEmail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean resetPassword(String userEmail, String recoveryCode, String newPassword) {
        String sql = "UPDATE user SET mdp = ? WHERE email_user = ? AND numeric_code = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(sql)){

            pst.setString(1, newPassword);
            pst.setString(2, userEmail);
            pst.setString(3, recoveryCode);

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Password reset successful for user with email: " + userEmail);
                return true;
            } else {
                System.out.println("Invalid recovery code or email: " + userEmail);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void sendForgotPasswordEmail(User user) {
        String numericCode = generateNumericCode();
        storeNumericCodeInDatabase(user, numericCode);

        String subject = "Password Reset";
        String body = "Dear " + user.getNom_user() + ",\n\n"
                + "To reset your password, please use the following numeric code: " + numericCode + "\n\n"
                + "If you did not request a password reset, please ignore this email.";

        String to = user.getEmail_user();
        String from = "mbarkih302@gmail.com"; // Replace with your email address
        String password = "apxn yakm zshr ture"; // Replace with your email password

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}