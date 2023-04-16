package at.setre14.library.model;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.tag.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Data {
    private List<Author> authors;
    private List<Book> books;
    private List<Series> series;
    private List<Tag> tags;
}
