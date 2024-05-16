package pidev.esprit.Entities;

public enum AdresseUser {
    ARIANA("ARIANA"),
    BEJA("BEJA"),
    BEN_AROUS("BEN_AROUS"),
    BIZERTE("BIZERTE"),
    GABES("GABES"),
    GAFSA("GAFSA"),
    JENDOUBA("JENDOUBA"),
    KAIROUAN("KAIROUAN"),
    KASSERINE("KASSERINE"),
    KEBILI("KEBILI"),
    KEF("KEF"),
    MAHDIA("MAHDIA"),
    MANOUBA("MANOUBA"),
    MEDENINE("MEDENINE"),
    MONASTIR("MONASTIR"),
    NABEUL("NABEUL"),
    SFAX("SFAX"),
    SIDI_BOUZID("SIDI_BOUZID"),
    SILIANA("SILIANA"),
    SOUSSE("SOUSSE"),
    TATAOUINE("TATAOUINE"),
    TOZEUR("TOZEUR"),
    TUNIS_CAPITALE("TUNIS_CAPITALE"),
    ZAGHOUAN("ZAGHOUAN");
    private String adresseUser;
    AdresseUser(String adresseUser) {
        this.adresseUser = adresseUser;



    }

    public String getAdresseUser() {
        return adresseUser;
    }
}
