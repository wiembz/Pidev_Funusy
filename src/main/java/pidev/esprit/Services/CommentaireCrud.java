package pidev.esprit.Services;


import pidev.esprit.Entities.Commentaire;
import pidev.esprit.Tools.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CommentaireCrud implements ICrud<Commentaire>  {
    Connection cnx2;

    public CommentaireCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterEntite(Commentaire c) {

        String req1 = "INSERT INTO commentaire(id_projet,contenue,date_commentaire) VALUES (?,?,?)";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req1);
            pst.setInt(1, c.getId_projet());
            pst.setString(2, c.getContenue());
            java.sql.Date sqlDate = java.sql.Date.valueOf(c.getDate_commentaire());
            pst.setDate(3, sqlDate);

            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }




    @Override
    public List<Commentaire> afficherEntite() {
        List<Commentaire> commentaires = new ArrayList<>();
        String req2 = "SELECT * FROM commentaire";
        try {
            Statement st2 = cnx2.createStatement();
            ResultSet rs = st2.executeQuery(req2);
            while (rs.next()) {
                Commentaire  C= new Commentaire();
                C.setId_commentaire(rs.getInt("id_commentaire"));
                C.setId_projet(rs.getInt("id_projet"));
                C.setContenue(rs.getString("contenue"));
                C.setDate_commentaire(rs.getDate("date_commentaire").toLocalDate());
                commentaires.add(C);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return commentaires;

    }

    @Override
    public boolean EntiteExists(Commentaire c) {
return true;
    }

    @Override
    public void updateEntite(Commentaire c) {
        String req2 = "UPDATE commentaire SET id_projet = ?, contenue = ?, date_commentaire = ? WHERE id_commentaire = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req2);
            pst.setInt(1, c.getId_projet());
            pst.setString(2, c.getContenue());

            // Convert LocalDate to java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(c.getDate_commentaire());
            pst.setDate(3, sqlDate);

            pst.setInt(4, c.getId_commentaire()); // id_commentaire doit être le dernier paramètre

            pst.executeUpdate();
            System.out.println("Commentaire modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }



    @Override
    public void deleteEntite(int id) {
        String req3 = "DELETE FROM commentaire WHERE id_commentaire = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req3);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Commentaire supprimée");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

}
