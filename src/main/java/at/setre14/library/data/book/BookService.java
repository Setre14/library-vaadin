package at.setre14.library.data.book;

import at.setre14.library.data.dbitem.DbItemService;
import org.springframework.stereotype.Service;

@Service
public class BookService extends DbItemService<Book> {

    public BookService(BookRepository repository) {
        super(repository);
    }

}
