package pidev.esprit.User.Entities;

public enum Role {
    ADMIN("ADMIN"),
    CLIENT("CLIENT");


    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
