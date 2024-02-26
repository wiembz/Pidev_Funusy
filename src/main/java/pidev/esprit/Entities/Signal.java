package pidev.esprit.Entities;

import java.time.LocalDate;

public class Signal {

    private int id_signal;
    private int id_commentaire;
    private String description;
    private LocalDate date_signal;


    public Signal(int id_signal, int id_commentaire, String description, LocalDate date_signal, boolean etat) {
        this.id_signal = id_signal;
        this.id_commentaire = id_commentaire;
        this.description = description;
        this.date_signal = date_signal;

    }
    public Signal(int id_commentaire, String description, LocalDate date_signal, boolean etat) {
        this.id_commentaire = id_commentaire;
        this.description = description;
        this.date_signal = date_signal;

    }
    public Signal() {

    }

    public int getId_signal() {
        return id_signal;
    }

    public int getId_commentaire() {
        return id_commentaire;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate_signal() {
        return date_signal;
    }



    public void setId_signal(int id_signal) {
        this.id_signal = id_signal;
    }

    public void setId_commentaire(int id_commentaire) {
        this.id_commentaire = id_commentaire;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate_signal(LocalDate date_signal) {
        this.date_signal = date_signal;
    }



    @Override
    public String toString() {
        return "Signal{" +
                "id_signal=" + id_signal +
                ", id_commentaire=" + id_commentaire +
                ", description='" + description + '\'' +
                ", date_signal=" + date_signal +

                '}';
    }
}
