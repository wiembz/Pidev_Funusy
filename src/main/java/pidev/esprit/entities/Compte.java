package pidev.esprit.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import static java.time.LocalTime.now;

public class Compte {

    private String rib;
    private double solde;
    private LocalDate  date_ouverture;
    private TypeCompte TP;
    public TypeCompte getTP() {
        return TP;
    }

    public void setTP(TypeCompte TP) {
        this.TP = TP;
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

   public Compte(String rib, double solde, TypeCompte TP) {
        this.rib = rib;
        this.solde = solde;
       this.date_ouverture = LocalDate.now();
        this.TP = TP;
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
        return "Compte{" +
                "rib='" + rib + '\'' +
                ", solde=" + solde +
                ", date_ouverture=" + date_ouverture +
                ","+ TypeCompte.EPARGNE +
        '}';
    }
}

