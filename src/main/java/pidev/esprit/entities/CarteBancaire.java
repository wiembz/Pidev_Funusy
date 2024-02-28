package pidev.esprit.entities;

import java.util.Date;
import java.util.Random;

public class CarteBancaire {
    private String num_carte;
    private int CVV2;
    private int code;
    private Date date_exp;
    private String rib;


    public CarteBancaire(String num_carte, int CVV2, int code, Date date_exp, String rib) {
        this.num_carte = num_carte;
        this.CVV2 = CVV2;
        this.code = code;
        this.date_exp = date_exp;
        this.rib = rib;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public CarteBancaire() {
    }

    public String getNum_carte() {
        return num_carte;
    }

    public void setNum_carte(String num_carte) {
        this.num_carte = num_carte;
    }

    public int getCVV2() {
        return CVV2;
    }

    public void setCVV2(int CVV2) {
        this.CVV2 = CVV2;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public java.sql.Date getDate_exp() {
        return (java.sql.Date) date_exp;
    }

    public void setDate_exp(Date date_exp) {
        this.date_exp = date_exp;
    }

    public static String generateNum() {
        StringBuilder ribBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int digit = (int) (Math.random() * 10); // Generate a random digit (0-9)
            ribBuilder.append(digit);
        }
        return ribBuilder.toString();
    }



    public static int generateCode() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Génère un nombre aléatoire entre 1000 et 9999
    }

    // Méthode pour générer un CVV2 aléatoire à 3 chiffres
    public static int generateCVV2() {
        Random random = new Random();
        return 100 + random.nextInt(900); // Génère un nombre aléatoire entre 100 et 999
    }




    @Override
    public String toString() {
        return
                "num_carte='" + num_carte + '\'' +
                ", CVV2=" + CVV2 +
                ", code=" + code +
                ", date_exp=" + date_exp +
                ", rib='" + rib ;
    }
}
