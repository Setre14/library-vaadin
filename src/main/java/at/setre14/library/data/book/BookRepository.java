package at.setre14.library.data.book;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, DbItemRepository<Book> {
    Page<Book> findByNameContainsIgnoreCaseOrAuthorIdInOrSeriesIdIn(String name, List<String> authorIds, List<String> seriesIds, Pageable pageable);
}
