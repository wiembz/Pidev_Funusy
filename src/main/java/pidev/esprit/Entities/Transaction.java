package pidev.esprit.Entities;

import java.security.SecureRandom;

public class Transaction {
    private int id;
    private Double montant;

    private String Source;
    private String Destination;

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

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    private enum TTR{TPE,Virement,Versement,Cheque,Prelevement};
    private TTR ttr;
    public Transaction(int id, Double montant, String source, String destination,TTR ttr) {
        this.id = id;
        this.montant = montant;
        this.Source = source;
        this.Destination = destination;
        this.ttr = ttr;
    }
}
