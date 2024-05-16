package pidev.esprit.Entities;

import java.io.File;
import java.util.Arrays;

public class Garantie extends Credit {
    private int Id_garantie;
    private int Id_credit;
    private String nature_garantie;
    private double Valeur_Garantie;
    private String preuve;


    public Garantie() {
    }

    public Garantie(int id_credit) {
        this.Id_credit = id_credit;
    }

    public Garantie(int Id_credit, String nature, double valeur, String data) {
        this.Id_credit = Id_credit;
        this.nature_garantie = nature;
        this.Valeur_Garantie = valeur;
        this.preuve = data;
    }

    public Garantie(String nature, double valeur, String data) {
        this.nature_garantie = nature;
        this.Valeur_Garantie = valeur;
        this.preuve = data;

    }
    public int getId_garantie() {
        return Id_garantie;
    }

    public void setId_garantie(int id_garantie) {
        Id_garantie = id_garantie;
    }

    @Override
    public int getId_credit() {
        return Id_credit;
    }

    @Override
    public void setId_credit(int id_credit) {
        Id_credit = id_credit;
    }

    public String getNature_garantie() {
        return nature_garantie;
    }
    public void setNature_garantie(String nature_garantie) {
        this.nature_garantie = nature_garantie;
    }

    public double getValeur_Garantie() {
        return Valeur_Garantie;
    }

    public void setValeur_Garantie(double valeur_Garantie) {
        Valeur_Garantie = valeur_Garantie;
    }

    public String getPreuve() {
        return preuve;
    }

    public void setPreuve(String preuve) {
        this.preuve = preuve;
    }

    @Override
    public String toString() {
        return "Garantie{" +
                "Id_garantie=" + Id_garantie +
                ", Id_credit=" + Id_credit +
                ", nature_garantie=" + nature_garantie +
                ", Valeur_Garantie=" + Valeur_Garantie +
                ", preuve='" + preuve + '\'' +
                '}';
    }

}
