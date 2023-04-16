package at.setre14.library.data.author;


import at.setre14.library.data.author.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<Author, String> {
}
