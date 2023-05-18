package at.setre14.library.data.user;

import at.setre14.library.data.Role;
import at.setre14.library.data.dbitem.DbItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document
@ToString
public class User extends DbItem {
    @Transient
    private String password;
    private String hashedPassword;
    private Role role;

    public User() {
        super();
    }

    public User(String name, String hashedPassword) {
        super(name);
        this.hashedPassword = hashedPassword;
    }
}
