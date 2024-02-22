package pidev.esprit.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import static java.time.LocalTime.now;

public class Compte {

    private String rib;
    private double solde;
    private LocalDate  date_ouverture;
    private String type_compte;

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

    public Compte(String rib, double solde, String type_compte) {
        this.rib = rib;
        this.solde = solde;
        this.type_compte = type_compte;
        this.date_ouverture = LocalDate.now();
    }

  /*public Compte(String rib, double solde, Date date_ouverture) {
        this.rib = rib;
        this.solde = solde;
        this.date_ouverture = date_ouverture;
    }*/

    public Compte() {
    }

    @Override
    public String toString() {
        return "RIB: " + rib + " Solde: " + solde + " Type compte= " + type_compte + "\n";
    }

}

