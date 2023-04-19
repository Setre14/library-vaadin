package at.setre14.library.data.tag;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends DbItemRepository<Tag> {
}
