package pidev.esprit.Entities;

public class Projet {
    private int id_projet;
    private int id_user;
    private String nom_projet;
    private float montant_req;
    private String type_projet;
    private String longitude;
    private String latitude;
    private String description;

    public Projet() {
    }

    public Projet(int id_projet, int id_user, String nom_projet, float montant_req, String type_projet, String longitude, String latitude , String description) {
        this.id_projet = id_projet;
        this.id_user = id_user;
        this.nom_projet = nom_projet;
        this.montant_req = montant_req;
        this.type_projet = type_projet;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }

    public Projet(int id_user, String nom_projet, float montant_req, String type_projet, String longitude, String latitude , String description) {
        this.id_user = id_user;
        this.nom_projet = nom_projet;
        this.montant_req = montant_req;
        this.type_projet = type_projet;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }

    public int getId_projet() {
        return id_projet;
    }

    public void setId_projet(int id_projet) {
        this.id_projet = id_projet;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNom_projet() {
        return nom_projet;
    }

    public void setNom_projet(String nom_projet) {
        this.nom_projet = nom_projet;
    }

    public float getMontant_req() {
        return montant_req;
    }

    public void setMontant_req(float montant_req) {
        this.montant_req = montant_req;
    }

    public String getType_projet() {
        return type_projet;
    }

    public void setType_projet(String type_projet) {
        this.type_projet = type_projet;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Projet{" +
                "id_projet=" + id_projet +
                ", id_user=" + id_user +
                ", nom_projet='" + nom_projet + '\'' +
                ", montant_req=" + montant_req +
                ", type_projet='" + type_projet + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", Description='" + description + '\'' +
                '}';
    }
}
