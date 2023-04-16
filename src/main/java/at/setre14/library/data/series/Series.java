package at.setre14.library.data.series;

import at.setre14.library.data.dbitem.DbItem;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
public class Series extends DbItem {
    public Series(String name) {
        super(name);
    }
}
