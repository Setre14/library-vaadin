package at.setre14.library.calibre;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.userbooksettings.UserBookSetting;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class CalibreImport {
    List<Author> authors = new LinkedList<>();
    List<Book> books = new LinkedList<>();
    List<Book> failedBooks = new LinkedList<>();
    List<Series> series = new LinkedList<>();
    List<Tag> tags = new LinkedList<>();
    List<UserBookSetting> userBookSettings = new LinkedList<>();

    public void merge(CalibreImport calibreImport) {
        authors.addAll(calibreImport.authors);
        books.addAll(calibreImport.books);
        failedBooks.addAll(calibreImport.failedBooks);
        series.addAll(calibreImport.series);
        tags.addAll(calibreImport.tags);
        userBookSettings.addAll(calibreImport.userBookSettings);
    }

    @Override
    public String toString() {
        return "CalibreImport{" +
                "authors=" + authors.size() +
                ", books=" + books.size() +
                ", failedBooks=" + failedBooks.size() +
                ", series=" + series.size() +
                ", tags=" + tags.size() +
                '}';
    }
}
