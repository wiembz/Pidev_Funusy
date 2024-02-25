package pidev.esprit.entities;

import java.time.LocalDate;

public class Compte {

    private String rib;
    private double solde;
    private LocalDate  date_ouverture;
    private String type_compte;
    private int id_user;

    public String getType_compte() {
        return type_compte;
    }

    public void setType_compte(String type_compte) {
        this.type_compte = type_compte;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public LocalDate getDate_ouverture() {
        return date_ouverture;
    }

    public void setDate_ouverture(LocalDate date_ouverture) {
        this.date_ouverture=date_ouverture;

    }

//    public Compte(String rib, double solde, String type_compte) {
//        this.rib = rib;
//        this.solde = solde;
//        this.type_compte = type_compte;
//        this.date_ouverture = LocalDate.now();
//    }
public static String generateRib() {
    StringBuilder ribBuilder = new StringBuilder();
    for (int i = 0; i < 20; i++) {
        int digit = (int) (Math.random() * 10); // Generate a random digit (0-9)
        ribBuilder.append(digit);
    }
    return ribBuilder.toString();
}


    public Compte(String rib,double solde, String type_compte,int id_user) {
        this.rib = generateRib();
        this.solde = solde;
        this.type_compte = type_compte;
        this.date_ouverture = LocalDate.now();
        this.id_user=id_user;

    }







  /*public Compte(String rib, double solde, Date date_ouverture) {
        this.rib = rib;
        this.solde = solde;
        this.date_ouverture = date_ouverture;
    }*/

    public Compte() {
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public String toString() {
        return "RIB: " + rib + " Solde: " + solde + " Type compte= " + type_compte +"\n";
    }

}

