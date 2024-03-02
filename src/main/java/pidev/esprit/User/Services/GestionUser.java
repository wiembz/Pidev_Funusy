package pidev.esprit.User.Services;

import pidev.esprit.User.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pidev.esprit.User.Tools.MyConnection;
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


}
