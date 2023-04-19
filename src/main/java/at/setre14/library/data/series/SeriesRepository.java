package at.setre14.library.data.series;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends DbItemRepository<Series> {
}
