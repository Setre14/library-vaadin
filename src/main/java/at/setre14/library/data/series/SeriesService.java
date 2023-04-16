package at.setre14.library.data.series;

import at.setre14.library.data.dbitem.DbItemService;
import org.springframework.stereotype.Service;

@Service
public class SeriesService extends DbItemService<Series> {

    public SeriesService(SeriesRepository repository) {
        super(repository);
    }

}
