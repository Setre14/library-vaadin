package at.setre14.library.data.author;

import at.setre14.library.data.dbitem.DbItem;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
public class Author extends DbItem {
    public Author(String name) {
        super(name);
    }
}
