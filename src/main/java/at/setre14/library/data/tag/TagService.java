package at.setre14.library.data.tag;

import at.setre14.library.data.dbitem.DbItemService;
import org.springframework.stereotype.Service;

@Service
public class TagService extends DbItemService<Tag> {

    public TagService(TagRepository repository) {
        super(repository);
    }

}
