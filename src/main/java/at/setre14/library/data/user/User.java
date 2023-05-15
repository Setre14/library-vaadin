package at.setre14.library.data.user;

import at.setre14.library.data.Role;
import at.setre14.library.data.dbitem.DbItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Document
public class User extends DbItem {
    private String hashedPassword;
    private Set<Role> roles;

    public User(String name, String hashedPassword) {
        super(name);
        this.hashedPassword = hashedPassword;
    }
}
