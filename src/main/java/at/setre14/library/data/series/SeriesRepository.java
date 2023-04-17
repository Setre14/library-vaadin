package at.setre14.library.data.series;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeriesRepository extends MongoRepository<Series, String>, DbItemRepository<Series> {
}
