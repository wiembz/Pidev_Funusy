package pidev.esprit.User.Entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

public class User {

    private int id_user;
    private String nom_user;
    private String prenom_user;
    private String email_user;
    private String mdp;
    private double salaire;
    private Date date_naissance;
    private int CIN;
    private int tel;
    private String adresse_user;
    private String role_user;
private String recoveryCode;
    public User() {
    }
    // Getter and Setter for recoveryCode
    public String getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    // Utility method to generate a recovery code
    public String generateRecoveryCode() {
        // Implement your logic to generate a recovery code
        // For example, you can use a random code generator
        // Make sure to store it in the recoveryCode field
        // Replace this with your actual implementation
        recoveryCode = String.valueOf((int) (Math.random() * 900000) + 100000); // Generates a random 6-digit code
        return recoveryCode;
    }
    public User(String nom_user, String prenom_user, String email_user, String mdp, double salaire, Date date_naissance, int CIN, int tel, String adresse_user, String role_user) {
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email_user = email_user;
        this.mdp = mdp;
        this.salaire = salaire;
        this.date_naissance = date_naissance;
        this.CIN = CIN;
        this.tel = tel;
        this.adresse_user = adresse_user;
        this.role_user = role_user;
    }

    public User(int id_user, String nom_user, String prenom_user, String email_user, String mdp, double salaire, Date date_naissance, int CIN, int tel, String adresse_user, String role_user) {
        this.id_user = id_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email_user = email_user;
        this.mdp = mdp;
        this.salaire = salaire;
        this.date_naissance = date_naissance;
        this.CIN = CIN;
        this.tel = tel;
        this.adresse_user = adresse_user;
        this.role_user = role_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNom_user() {
        return nom_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }

    public String getPrenom_user() {
        return prenom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }



    public int getCIN() {
        return CIN;
    }

    public void setCIN(int CIN) {
        this.CIN = CIN;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getAdresse_user() {
        return adresse_user;
    }

    public void setAdresse_user(String adresse_user) {
        this.adresse_user = adresse_user;
    }

    public String getRole_user() {
        return role_user;
    }

    public void setRole_user(String role_user) {
        this.role_user = role_user;
    }

// ... (Other constructors, getters, and setters)

    // Utility method to convert Date to Date
    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Date getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(Date date_naissance) {
        this.date_naissance = date_naissance;
    }
// ... (Other methods)


    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", nom_user='" + nom_user + '\'' +
                ", prenom_user='" + prenom_user + '\'' +
                ", email_user='" + email_user + '\'' +
                ", mdp='" + mdp + '\'' +
                ", salaire=" + salaire +
                ", date_naissance=" + date_naissance +
                ", CIN=" + CIN +
                ", tel=" + tel +
                ", adresse_user='" + adresse_user + '\'' +
                ", role_user='" + role_user + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getId_user() == user.getId_user() && Double.compare(getSalaire(), user.getSalaire()) == 0 && getCIN() == user.getCIN() && getTel() == user.getTel() && Objects.equals(getNom_user(), user.getNom_user()) && Objects.equals(getPrenom_user(), user.getPrenom_user()) && Objects.equals(getEmail_user(), user.getEmail_user()) && Objects.equals(getMdp(), user.getMdp()) && Objects.equals(getDate_naissance(), user.getDate_naissance()) && Objects.equals(getAdresse_user(), user.getAdresse_user()) && Objects.equals(getRole_user(), user.getRole_user());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_user(), getNom_user(), getPrenom_user(), getEmail_user(), getMdp(), getSalaire(), getDate_naissance(), getCIN(), getTel(), getAdresse_user(), getRole_user());
    }
}
