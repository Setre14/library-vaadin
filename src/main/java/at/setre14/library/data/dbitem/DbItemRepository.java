package at.setre14.library.data.dbitem;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface DbItemRepository<T extends DbItem> extends MongoRepository<T, String> {
    T findByName(String name);
    List<T> findByNameContainsIgnoreCase(String name);
    Page<T> findByNameContainsIgnoreCase(String name, Pageable pageable);
}
