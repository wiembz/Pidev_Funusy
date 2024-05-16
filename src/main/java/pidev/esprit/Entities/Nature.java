package pidev.esprit.Entities;

public enum Nature {
    Maison("Maison"),
    Voiture("Voiture"),
    Terrain("Terrain"),
    LocalCommercial("LocalCommercial");

    private final String typeName;

    Nature(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isEmpty() {
        return typeName == null || typeName.isEmpty();
    }
}
