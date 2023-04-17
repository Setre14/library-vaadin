package at.setre14.library.data.tag;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<Tag, String>, DbItemRepository<Tag> {
}
