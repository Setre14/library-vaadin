package at.setre14.library.data.author;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends DbItemRepository<Author> {
}
