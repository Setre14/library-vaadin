package at.setre14.library.views.book;

import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.views.authors.AuthorView;
import at.setre14.library.views.series.SeriesView;
import at.setre14.library.views.tags.TagView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.function.Function;

public class BookListGrid  extends Div {

    private final Grid<Book> grid;
    private final BookService bookService;
    private final BookListFilter bookListFilter;
    private final Paginator paginator;

    public BookListGrid(AuthorService authorService, BookService bookService, SeriesService seriesService, TagService tagService) {
        this.bookService = bookService;

        setSizeFull();

        bookListFilter = new BookListFilter(authorService.findAll(), seriesService.findAll(), tagService.findAll(), this::setGridItems);
        paginator = new Paginator(this::setGridItems);

        grid = new Grid<>(Book.class, false);
        grid.addColumn(createLinkHeaderComponentRenderer(BookView.class, book -> book)).setHeader("Title").setAutoWidth(true);
        grid.addColumn(createLinkHeaderComponentRenderer(AuthorView.class, Book::getAuthor)).setHeader("Author").setAutoWidth(true);
        grid.addColumn(createLinkHeaderComponentRenderer(SeriesView.class, Book::getSeries)).setHeader("Series").setAutoWidth(true);
        grid.addColumn(createTagsComponentRenderer()).setHeader("Tags").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        setGridItems();

        VerticalLayout layout = new VerticalLayout(bookListFilter, grid, paginator);
        layout.setSizeFull();

        add(layout);
    }

    private ComponentRenderer<RouterLink, Book> createLinkHeaderComponentRenderer(Class viewClass, Function<Book, DbItem> dbItemFunc) {
        return new ComponentRenderer<>(book -> {
            DbItem item = dbItemFunc.apply(book);
            String id = item == null ? "" : item.getId();
            String text = item == null ? "" : item.getName();

            RouterLink link = new RouterLink(text, viewClass, id);

            link.addClassName("grid-cell-link");

            return link;
        });
    }
    private ComponentRenderer<HorizontalLayout, Book> createTagsComponentRenderer() {
        return new ComponentRenderer<>(book -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            for(Tag tag: book.getSortedTags()) {
                RouterLink link = new RouterLink(tag.toString(), TagView.class, tag.getId());
                link.getElement().getThemeList().add("badge contrast");
                link.addClassName("grid-cell-link");

                horizontalLayout.add(link);
            }
            return horizontalLayout;
        });
    }

    private void setGridItems() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Page<Book> page = bookService.list(bookListFilter.getBookFilter(), paginator.getPageRequest(order));
        paginator.update(page);

        grid.setItems(page.getContent());
        refreshGrid();

        paginator.validate();
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
