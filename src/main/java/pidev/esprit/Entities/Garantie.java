package pidev.esprit.Entities;

import java.util.Arrays;

public class Garantie extends Credit {
    private int Id_garantie;
    private int Id_credit;
    private Nature nature;
    private double valeur;
    private String data;


    public Garantie() {
    }

    public Garantie(int id_credit) {
        this.Id_credit = id_credit;
    }

    public Garantie(int Id_credit, Nature nature, double valeur, String data) {
        this.Id_credit = Id_credit;
        this.nature = nature;
        this.valeur = valeur;
        this.data = data;
    }

    public Garantie(Nature nature, double valeur, String data) {
        this.nature = nature;
        this.valeur = valeur;
        this.data = data;

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

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Garantie{" +
                "Id_garantie=" + Id_garantie +
                ", Id_credit=" + Id_credit +
                ", nature=" + nature +
                ", valeur=" + valeur +
                ", data='" + data + '\'' +
                '}';
    }

    public enum Nature {
        Maison, Voiture, Terrain, LocalCommercial
    }
}
