package at.setre14.library.data.dbitem;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.author.AuthorRepository;
import at.setre14.library.data.entity.SamplePerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class DbItemService<T> {

    private final MongoRepository<T, String> repository;

    public DbItemService(MongoRepository<T, String> repository) {
        this.repository = repository;
    }
    public List<T> findAll() {
        return repository.findAll();
    }
    public Page<T> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void save(T item) {
        repository.save(item);
    }
    public void saveAll(Iterable<T> items) {
        repository.saveAll(items);
    }

    public int count() {
        return (int) repository.count();
    }

}
