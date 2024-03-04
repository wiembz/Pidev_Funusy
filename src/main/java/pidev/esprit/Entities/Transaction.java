package pidev.esprit.Entities;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Transaction {
    public String getSource() {
        return Source;
    }

    public String getDestination() {
        return Destination;
    }

    private int id;

    private Double montant;

    private String Source;
    private String Destination;

    private String[] TTR = {"VIREMENT","VERSEMENT","CHEQUE","PRELEVEMENT","TRANSFERT"};

    public Transaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setSource(String source) {
        Source = source;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String[] getTTR() {
        return TTR;
    }

    public void setTTR(String[] TTR) {
        this.TTR = TTR;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }


    public Transaction(int id, Double montant, String source, String destination, int index) {
        this.id = id;
        this.montant = montant;
        Source = source;
        Destination = destination;
        this.TTR[index] = TTR[index];
    }
    public Transaction( Double montant, String source, String destination, int index) {

        this.montant = montant;
        Source = source;
        Destination = destination;
        this.TTR[index] = TTR[index];
    }
}
