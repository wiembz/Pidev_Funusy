package pidev.esprit.entities;

import java.util.Date;
import java.util.Objects;

public class User {

    public User() {
    }

    int id_user;

    String nom_user ;
    String prenom_user ;
    String email_user;
    int CIN;
    int tel;
    String mdp;
    public enum Role {
        CLIENT, ADMIN
    }
    Role role_user;
    double salaire;
    Date date_naissance;

    String adresse_user;

    public User(int id_user, String nom_user, String prenom_user, String email_user, String mdp, Role role_user, double salaire, Date date_naissance, int CIN, int tel, String adresse_user) {
        this.id_user = id_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email_user = email_user;
        this.mdp = mdp;
        this.role_user = role_user;
        this.salaire = salaire;
        this.date_naissance = date_naissance;
        this.CIN = CIN;
        this.tel = tel;
        this.adresse_user = adresse_user;
    }

    public User(String nom_user, String prenom_user, String email_user, String mdp, Role role_user, double salaire, Date date_naissance, int CIN, int tel, String adresse_user) {
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email_user = email_user;
        this.mdp = mdp;
        this.role_user = role_user;
        this.salaire = salaire;
        this.date_naissance = date_naissance;
        this.CIN = CIN;
        this.tel = tel;
        this.adresse_user = adresse_user;
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

    public Role getRole_user() {
        return role_user;
    }

    public void setRole_user(Role role_user) {
        this.role_user = role_user;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public Date getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(Date date_naissance) {
        this.date_naissance = date_naissance;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getId_user() == user.getId_user() && Double.compare(getSalaire(), user.getSalaire()) == 0 && getCIN() == user.getCIN() && getTel() == user.getTel() && Objects.equals(getNom_user(), user.getNom_user()) && Objects.equals(getPrenom_user(), user.getPrenom_user()) && Objects.equals(getEmail_user(), user.getEmail_user()) && Objects.equals(getMdp(), user.getMdp()) && getRole_user() == user.getRole_user() && Objects.equals(getDate_naissance(), user.getDate_naissance()) && Objects.equals(getAdresse_user(), user.getAdresse_user());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_user(), getNom_user(), getPrenom_user(), getEmail_user(), getMdp(), getRole_user(), getSalaire(), getDate_naissance(), getCIN(), getTel(), getAdresse_user());
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", nom_user='" + nom_user + '\'' +
                ", prenom_user='" + prenom_user + '\'' +
                ", email_user='" + email_user + '\'' +
                ", mdp='" + mdp + '\'' +
                ", role_user=" + role_user +
                ", salaire=" + salaire +
                ", date_naissance=" + date_naissance +
                ", CIN=" + CIN +
                ", tel=" + tel +
                ", adresse_user='" + adresse_user + '\'' +
                '}';
    }
}
