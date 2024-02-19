package pidev.esprit.Entities;


import java.util.Date;

public class Investissement {
    int id;
    int id_user;
    float montant;
    Date date_inv;
    int periode;
    public Investissement() {
    }

    public Investissement(int id, int id_user, float montant, Date date_inv, int periode) {
        this.id = id;
        this.id_user = id_user;
        this.montant = montant;
        this.date_inv = date_inv;
        this.periode = periode;
    }

    public Investissement(int id_user, float montant, Date date_inv, int periode) {
        this.id_user = id_user;
        this.montant = montant;
        this.date_inv = date_inv;
        this.periode = periode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public Date getDate_inv() {
        return date_inv;
    }

    public void setDate_inv(Date date_inv) {
        this.date_inv = date_inv;
    }

    public int getPeriode() {
        return periode;
    }

    public void setPeriode(int periode) {
        this.periode = periode;
    }

    @Override
    public String toString() {
        return "Investissement{" +
                "id=" + id +
                ", id_user=" + id_user +
                ", montant=" + montant +
                ", date_inv=" + date_inv +
                ", periode=" + periode +
                '}';
    }
}
