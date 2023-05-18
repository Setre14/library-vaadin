package at.setre14.library.data.dbitem;

import at.setre14.library.components.grid.DbItemGridFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public abstract class DbItemService<T extends DbItem> {

    protected final DbItemRepository<T> repository;

    public DbItemService(DbItemRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        return repository.findAll(Sort.by(order));
    }

    public Page<T> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<T> list(DbItemGridFilter<T> filter, Pageable pageable) {
        return repository.findByNameContainsIgnoreCase(filter.getNameFilter(), pageable);
    }

    public T findById(String id) {
        Optional<T> item = repository.findById(id);

        return item.orElse(null);
    }


    public List<T> filterByName(String name) {
        return repository.findByNameContainsIgnoreCase(name);
    }

    public List<T> findAllById(List<String> ids) {
        Iterable<T> iterable = repository.findAllById(ids);
        List<T> result = new ArrayList<>();
        iterable.forEach(result::add);

        return result;
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
