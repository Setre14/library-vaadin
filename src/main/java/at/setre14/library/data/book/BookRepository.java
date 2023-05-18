package at.setre14.library.data.book;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends DbItemRepository<Book> {


    Page<Book> findByNameContainsIgnoreCaseAndAuthorId(String name, String authorId, Pageable pageable);

    Page<Book> findByNameContainsIgnoreCaseAndSeriesId(String name, String seriesId, Pageable pageable);

    Page<Book> findByNameContainsIgnoreCaseAndTagIdsContains(String name, String tagId, Pageable pageable);

    Page<Book> findByNameContainsIgnoreCaseAndAuthorIdAndSeriesId(String name, String authorId, String seriesId, Pageable pageable);

    Page<Book> findByNameContainsIgnoreCaseAndAuthorIdAndTagIdsContains(String name, String authorId, String tagId, Pageable pageable);

    Page<Book> findByNameContainsIgnoreCaseAndSeriesIdAndTagIdsContains(String name, String seriesId, String tagId, Pageable pageable);

    Page<Book> findByNameContainsIgnoreCaseAndAuthorIdAndSeriesIdAndTagIdsContains(String name, String authorId, String seriesId, String tagId, Pageable pageable);

}
