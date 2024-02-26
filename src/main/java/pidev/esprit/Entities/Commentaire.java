package pidev.esprit.Entities;

import java.time.LocalDate;

public class Commentaire {
    private int id_commentaire;
    private int id_projet;
    private String contenue;
    private LocalDate date_commentaire;

    public Commentaire(int id_commentaire, int id_projet, String contenue, LocalDate date_commentaire) {
        this.id_commentaire = id_commentaire;
        this.id_projet = id_projet;
        this.contenue = contenue;
        this.date_commentaire = date_commentaire;
    }

    public Commentaire(int id_projet, String contenue) {
        this.id_projet = id_projet;
        this.contenue = contenue;
    }

    public Commentaire() {

    }

    public int getId_commentaire() {
        return id_commentaire;
    }

    public int getId_projet() {
        return id_projet;
    }

    public String getContenue() {
        return contenue;
    }

    public LocalDate getDate_commentaire() {
        return date_commentaire;
    }

    public void setId_commentaire(int id_commentaire) {
        this.id_commentaire = id_commentaire;
    }

    public void setId_projet(int id_projet) {
        this.id_projet = id_projet;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }

    public void setDate_commentaire(LocalDate date_commentaire) {
        this.date_commentaire = date_commentaire;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id_commentaire=" + id_commentaire +
                ", id_projet=" + id_projet +
                ", contenue='" + contenue + '\'' +
                ", date_commentaire=" + date_commentaire +
                '}';
    }


}



