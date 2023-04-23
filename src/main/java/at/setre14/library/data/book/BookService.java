package at.setre14.library.data.book;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.dbitem.DbItemService;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.data.userbooksettings.UserBookSettingService;
import at.setre14.library.components.grid.BookGridFilter;
import at.setre14.library.components.grid.DbItemGridFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    public Book findById(String id) {
        Optional<Book> item = repository.findById(id);

        return item.map(this::initBook).orElse(null);
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
    public Page<Book> list(DbItemGridFilter<Book> filter, Pageable pageable) {
        if(filter instanceof BookGridFilter bookGridFilter) {
            Page<Book> page;
            BookRepository bookRepository = (BookRepository) repository;

            String title = filter.getNameFilter();

            Author author = bookGridFilter.getAuthorFilter();
            Series series = bookGridFilter.getSeriesFilter();
            Tag tag = bookGridFilter.getTagFilter();

            if(author != null) {
                if (series != null) {
                    if (tag != null) {
                        page = bookRepository.findByNameContainsIgnoreCaseAndAuthorIdAndSeriesIdAndTagIdsContains(title, author.getId(), series.getId(), tag.getId(), pageable);
                    } else {
                        page = bookRepository.findByNameContainsIgnoreCaseAndAuthorIdAndSeriesId(title, author.getId(), series.getId(), pageable);
                    }
                } else if (tag != null) {
                    page = bookRepository.findByNameContainsIgnoreCaseAndAuthorIdAndTagIdsContains(title, author.getId(), tag.getId(), pageable);
                } else {
                    page = bookRepository.findByNameContainsIgnoreCaseAndAuthorId(title, author.getId(), pageable);
                }
            } else if(series != null) {
                if (tag != null) {
                    page = bookRepository.findByNameContainsIgnoreCaseAndSeriesIdAndTagIdsContains(title, series.getId(), tag.getId(), pageable);
                } else {
                    page = bookRepository.findByNameContainsIgnoreCaseAndSeriesId(title, series.getId(), pageable);
                }
            } else if(tag != null) {
                page = bookRepository.findByNameContainsIgnoreCaseAndTagIdsContains(title, tag.getId(), pageable);
            } else {
                page = bookRepository.findByNameContainsIgnoreCase(title, pageable);
            }

            List<Book> books = page.get().map(this::initBook).toList();
            System.out.println(books.size());

            return page;
        } else {
            return super.list(filter, pageable);
        }

    }

}
