package at.setre14.library.data.dbitem;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

@Getter
public abstract class DbItem {
    @Id
    protected String id;
    @Indexed(unique = true)
    @Setter
    protected String name;

    protected DbItem() {
        this.id = UUID.randomUUID().toString();
    }

    protected DbItem(String name) {
        this();
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
