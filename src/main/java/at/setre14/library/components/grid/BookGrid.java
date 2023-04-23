package at.setre14.library.components.grid;

import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.views.authors.AuthorView;
import at.setre14.library.views.book.BookView;
import at.setre14.library.views.series.SeriesView;
import at.setre14.library.views.tags.TagView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class BookGrid extends DbItemGrid<Book> {
    private final AuthorService authorService;
    private final SeriesService seriesService;
    private final TagService tagService;

    public BookGrid(BookService bookService, AuthorService authorService, SeriesService seriesService, TagService tagService) {
        super(bookService, Book.class, BookView.class);
        this.authorService = authorService;
        this.seriesService = seriesService;
        this.tagService = tagService;
    }

    @Override
    protected DbItemGridFilter<Book> createFilter() {
        return new BookGridFilter(authorService.findAll(), seriesService.findAll(), tagService.findAll(), this::setGridItems).init();
    }

    @Override
    protected Grid<Book> createGrid() {
        grid = new Grid<>(Book.class, false);
        grid.addColumn(createLinkHeaderComponentRenderer(book -> book, BookView.class)).setHeader("Title").setAutoWidth(true);
        grid.addColumn(createLinkHeaderComponentRenderer(Book::getAuthor, AuthorView.class)).setHeader("Author").setAutoWidth(true);
        grid.addColumn(createLinkHeaderComponentRenderer(Book::getSeries, SeriesView.class)).setHeader("Series").setAutoWidth(true);
        grid.addColumn(createTagsComponentRenderer()).setHeader("Tags").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        setGridItems();

        return grid;
    }

    private ComponentRenderer<HorizontalLayout, Book> createTagsComponentRenderer() {
        return new ComponentRenderer<>(book -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            for(Tag tag: book.getSortedTags()) {
                RouterLink link = new RouterLink(tag.toString(), TagView.class, tag.getId());
                link.addClassName("black-link");
                link.getElement().getThemeList().add("badge contrast");

                horizontalLayout.add(link);
            }
            return horizontalLayout;
        });
    }
}
