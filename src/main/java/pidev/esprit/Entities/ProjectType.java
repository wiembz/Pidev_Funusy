package pidev.esprit.Entities;

public enum ProjectType{
    AGRICULTURE("Agriculture"),
    TECHNOLOGIQUE("Technologique"),
    BOURSE("Bourse"),
    IMMOBILIER("Immobilier");

    private final String typeName;

    ProjectType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
