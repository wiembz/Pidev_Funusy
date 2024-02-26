package pidev.esprit.Entities;

import java.util.Date;

public class Credit {
    private int Id_credit;
    private double Montant_credit;
    private double Taux_credit;
    private int Duree_credit;
    private Date date_credit;
    private int id_user;

    public Credit() {
    }

    public Credit(int Id) {
        this.Id_credit = Id;
    }

    public Credit(double Montant, int duree, double taux) {
        this.Montant_credit = Montant;
        this.Duree_credit = duree;
        this.Taux_credit = taux;
    }

    public Credit(double Montant, int duree, double taux, int Id_user) {
        this.Montant_credit = Montant;
        this.Duree_credit = duree;
        this.Taux_credit = taux;
        this.id_user = Id_user;
    }

    public Credit(int Id, double Montant, int Duree, double Taux) {
        this.Id_credit = Id;
        this.Montant_credit = Montant;
        this.Duree_credit = Duree;
        this.Taux_credit = Taux;

    }

    public int getId_credit() {
        return Id_credit;
    }

    public void setId_credit(int id_credit) {
        Id_credit = id_credit;
    }

    public double getMontant_credit() {
        return Montant_credit;
    }

    public void setMontant_credit(double montant_credit) {
        Montant_credit = montant_credit;
    }

    public double getTaux_credit() {
        return Taux_credit;
    }

    public void setTaux_credit(double taux_credit) {
        Taux_credit = taux_credit;
    }

    public int getDuree_credit() {
        return Duree_credit;
    }

    public void setDuree_credit(int duree_credit) {
        Duree_credit = duree_credit;
    }

    public Date getDate_credit() {
        return date_credit;
    }

    public void setDate_credit(Date date_credit) {
        this.date_credit = date_credit;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "Id_credit=" + Id_credit +
                ", Montant_credit=" + Montant_credit + "DT"+
                ", Taux_credit=" + Taux_credit + "% "+
                ", Duree_credit=" + Duree_credit +
                ", date_credit=" + date_credit +
                '}';
    }
}
