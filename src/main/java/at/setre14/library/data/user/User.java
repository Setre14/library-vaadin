package at.setre14.library.model;

import at.setre14.library.data.dbitem.DbItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class User extends DbItem {
    @JsonIgnore
    private String password;

    private Boolean syncDefault;

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
        this.syncDefault = false;
    }

    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }
}
