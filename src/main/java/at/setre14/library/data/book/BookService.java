package at.setre14.library.data.book;

import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.dbitem.DbItemService;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.data.userbooksettings.UserBookSettingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class BookService extends DbItemService<Book> {


    private final AuthorService authorService;
    private final SeriesService seriesService;
    private final TagService tagService;
    private final UserBookSettingService userBookSettingService;

    public BookService(
            BookRepository repository,
            AuthorService authorService,
            SeriesService seriesService,
            TagService tagService,
            UserBookSettingService userBookSettingService
    ) {
        super(repository);
        this.authorService = authorService;
        this.seriesService = seriesService;
        this.tagService = tagService;
        this.userBookSettingService = userBookSettingService;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = super.findAll();

        for(Book book: books) {
            book.setAuthor(authorService.findById(book.getAuthorId()));
            book.setSeries(seriesService.findById(book.getSeriesId()));
            book.setTags(new HashSet<>(tagService.findAllById(book.getTagIds())));
        }

        return books;
    }

    private Book initBook(Book book) {
        book.setAuthor(authorService.findById(book.getAuthorId()));
        book.setSeries(seriesService.findById(book.getSeriesId()));
        book.setTags(new HashSet<>(tagService.findAllById(book.getTagIds())));

        return book;
    }

    @Override
    public Page<Book> list(Pageable pageable) {
        Page<Book> page = super.list(pageable);

        List books = page.get().map(this::initBook).toList();
        System.out.println(books.size());

        return page;
    }

}
