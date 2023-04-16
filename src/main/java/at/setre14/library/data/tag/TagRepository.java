package at.setre14.library.data.tag;


import at.setre14.library.data.book.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<Tag, String> {
}
