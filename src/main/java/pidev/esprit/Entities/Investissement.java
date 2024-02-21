package pidev.esprit.Entities;


import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Date;

public class Investissement {
    private  int id_investissement;
    private  int id_user;
    private  float montant;
    private  Date date_investissement;
    private  int periode;
    public Investissement() {
    }

    public Investissement(int id_investissement, int id_user, float montant, Date date_investissement, int periode) {
        this.id_investissement = id_investissement;
        this.id_user = id_user;
        this.montant = montant;
        this.date_investissement = date_investissement;
        this.periode = periode;
    }

    public Investissement(int id_user, float montant, Date date_investissement, int periode) {
        this.id_user = id_user;
        this.montant = montant;
        this.date_investissement = date_investissement;
        this.periode = periode;
    }



    public int getId_investissement() {
        return id_investissement;
    }

    public void setId_investissement(int id_investissement) {
        this.id_investissement = id_investissement;
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

    public Date getDate_investissement() {
        return date_investissement;
    }

    public void setDate_investissement(Date date_investissement) {
        this.date_investissement = date_investissement;
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
                "id=" + id_investissement +
                ", id_user=" + id_user +
                ", montant=" + montant +
                ", date_investissement=" + date_investissement +
                ", periode=" + periode +
                '}';
    }
}
