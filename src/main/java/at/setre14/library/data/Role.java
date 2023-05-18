package at.setre14.library.data;

import lombok.Getter;

@Getter
public enum Role {
    USER("User"), ADMIN("Admin");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
