package at.setre14.library.data.dbitem;


import java.util.List;

public interface DbItemRepository<T>{
    List<T> findAllByNameContainsIgnoreCase(String name);
}
