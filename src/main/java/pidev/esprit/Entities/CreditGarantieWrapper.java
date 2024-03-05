package pidev.esprit.Entities;

public class CreditGarantieWrapper extends Credit {
    private Credit credit;
    private Garantie garantie;

    public CreditGarantieWrapper(Credit credit, Garantie garantie) {
        this.credit = credit;
        this.garantie = garantie;
    }

    // Getters pour les attributs de crédit
    public int getId_credit() {
        return credit.getId_credit();
    }

    public double getMontant_credit() {
        return credit.getMontant_credit();
    }

    public int getDuree_credit() {
        return credit.getDuree_credit();
    }

    public double getTaux_credit() {
        return credit.getTaux_credit();
    }

    public String getStatus() {
        return credit.getStatus();
    }


    // Getters pour les attributs de garantie
    public int getId_garantie() {
        return garantie.getId_garantie();
    }

    public String getNature_garantie() {
        return garantie.getNature_garantie();
    }

    public double getValeur_Garantie() {
        return garantie.getValeur_Garantie();
    }

    public String getPreuve() {
        return garantie.getPreuve();
    }

    public Credit getCredit() {
        return credit;
    }

    public CreditGarantieWrapper getGarantie() {
        return this;
    }

    // Ajoutez d'autres getters si nécessaire
}


