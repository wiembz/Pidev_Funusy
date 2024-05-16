package pidev.esprit.Services;

import lombok.Getter;
import pidev.esprit.Tools.MyConnection;
import pidev.esprit.Entities.CarteBancaire;
import pidev.esprit.Entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static pidev.esprit.Controllers.Account.TwilioService.sendSMS;
import static pidev.esprit.Services.CompteCrud.retrieveUserByRIB;

public class CardCrud implements CRUDC<CarteBancaire> {
    public static Connection cnx2;
    private CarteBancaire c;
    private String rib;

    public CardCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }


    public void addCard(CarteBancaire c, String rib) {
        c.setNum_carte(CarteBancaire.generateNum());
        c.setCode(CarteBancaire.generateCode());
        c.setCVV2(CarteBancaire.generateCVV2());

        String sql = "INSERT INTO carte_bancaire (num_carte, code, CVV2, date_exp, rib) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = cnx2.prepareStatement(sql);
            pstmt.setString(1, c.getNum_carte());
            pstmt.setInt(2, c.getCode());
            pstmt.setInt(3, c.getCVV2());
            pstmt.setDate(4, new java.sql.Date(c.getDate_exp().getTime()));
            pstmt.setString(5, rib);

            int rowsInserted = pstmt.executeUpdate(); // Check number of rows inserted

            if (rowsInserted > 0) {
                System.out.println("Carte bancaire ajoutée pour le compte avec RIB " + rib);
            } else {
                System.out.println("Aucune carte bancaire ajoutée pour le compte avec RIB " + rib);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la carte bancaire : " + e.getMessage());
        }
    }


    @Override
    public List<CarteBancaire> displayCard() {
        List<CarteBancaire> cards = new ArrayList<>();
        String sql = "SELECT * FROM carte_bancaire";

        try {
            PreparedStatement pstmt = cnx2.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                CarteBancaire card = new CarteBancaire();
                card.setNum_carte(rs.getString("num_carte"));
                card.setCode(rs.getInt("code"));
                card.setCVV2(rs.getInt("cvv2"));
                card.setDate_exp(rs.getDate("date_exp"));
                card.setRib(rs.getString("rib"));
                cards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cartes bancaires " + e.getMessage());
        }
        return cards;
    }

    @Getter
    private static final CardCrud instance = new CardCrud();


    @Override
    public void updateCard(CarteBancaire c) {
        String sql = "UPDATE carte_bancaire SET code = ?, CVV2 = ?, date_exp = ? WHERE num_carte = ?";

        try {
            PreparedStatement pstmt = cnx2.prepareStatement(sql);
            pstmt.setInt(1, c.getCode());
            pstmt.setInt(2, c.getCVV2());
            //pstmt.setDate(3, c.getDate_exp());

            pstmt.setString(4, c.getNum_carte());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Carte bancaire mise à jour.");
            } else {
                System.out.println("Pas de carte bancaire trouvée avec ce numéro.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la carte bancaire : " + e.getMessage());
        }

    }

    public static void deleteCard(String num) {
        String sql = "DELETE FROM carte_bancaire WHERE num_carte = ?";

        try {
            Connection connection = MyConnection.getInstance().getCnx();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, num);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Carte bancaire supprimée.");
            } else {
                System.out.println("Pas de carte bancaire trouvée avec ce numéro.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la carte bancaire : " + e.getMessage());
        }
    }

    public boolean cardExistsForUser(String rib) {
        boolean cardExists = false;

        try {
            String sql = "SELECT COUNT(*) AS count FROM carte_bancaire WHERE rib = ?";
            Connection connection = MyConnection.getInstance().getCnx();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rib);

            // Execute query
            ResultSet rs = pstmt.executeQuery();

            // Check if any card exists for the given RIB
            if (rs.next()) {
                int count = rs.getInt("count");
                cardExists = count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }

        return cardExists;
    }


    //*********** SMS TWILIO ***********//
    public static void sendExpirationReminders() {
        System.out.println("fl appel");
        List<CarteBancaire> cards = CardCrud.instance.displayCard();
        for (CarteBancaire card : cards)
            if (isExpiringSoon(card))
                sendReminder(card);
    }

    public static boolean isExpiringSoon(CarteBancaire card) {
        // Get today's date without considering time
        LocalDate today = LocalDate.now();

        // Get the expiration date of the card without considering time
        LocalDate expirationDate = card.getDate_exp().toLocalDate();

        // Calculate the difference between today's date and the expiration date
        long daysUntilExpiration = java.time.temporal.ChronoUnit.DAYS.between(today, expirationDate);

        // Check if the expiration date is within 30 days from today
        return daysUntilExpiration == 0;
    }

    public static void sendReminder(CarteBancaire card) {
        System.out.println("reminder");
        String message = "Dear our customer, your credit card with number " + card.getNum_carte() + " is expiring in 30 days . Please renew it.";

        // Retrieve the customer's phone number based on the card's RIB (assuming you have a method for that)
        String phoneNumber = getPhoneNumberByRIB(card.getRib());

        // Send the reminder message using Twilio
        sendSMS(phoneNumber, message);
    }

    // Method to retrieve the customer's phone number based on the card's RIB
    private static String getPhoneNumberByRIB(String rib) {
        User user = retrieveUserByRIB(rib);
        if (user != null) {
            int tel = user.getTel();
            System.out.println(tel);
            return String.valueOf(tel);
        } else {
            System.out.println("User not found for RIB: " + rib);
            return ""; // or handle the case where user is not found
        }
    }


}