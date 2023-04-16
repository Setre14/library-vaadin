package at.setre14.library.data.tag;

import at.setre14.library.data.dbitem.DbItem;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
public class Tag extends DbItem {
    public Tag(String name) {
        super(name);
    }
}
