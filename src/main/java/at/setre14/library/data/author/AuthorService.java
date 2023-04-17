package at.setre14.library.data.author;

import at.setre14.library.data.dbitem.DbItemService;
import org.springframework.stereotype.Service;

@Service
public class AuthorService extends DbItemService<Author> {

    public AuthorService(AuthorRepository repository) {
        super(repository);
    }
    
}
