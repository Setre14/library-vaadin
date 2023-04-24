package at.setre14.library.views.dbitem;

import at.setre14.library.components.grid.BookGrid;
import at.setre14.library.components.grid.DbItemGrid;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.dbitem.DbItemService;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.TagService;
import com.vaadin.flow.component.html.DescriptionList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;

public abstract class DbItemView<T extends DbItem> extends Div implements HasUrlParameter<String> {
    private final DbItemService<T> service;
    private final BookService bookService;
    private final AuthorService authorService;
    private final SeriesService seriesService;
    private final TagService tagService;


    protected String id;
    protected T item;

    public DbItemView(DbItemService<T> service, BookService bookService, AuthorService authorService, SeriesService seriesService, TagService tagService) {
        this.service = service;
        this.bookService = bookService;
        this.authorService = authorService;
        this.seriesService = seriesService;
        this.tagService = tagService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String id)  {

        if (id == null) {
            this.item = createItem();
            this.id = item.getId();
        } else {
            this.id = id;
            this.item = service.findById(id);
        }

        createPage();
    }

    protected abstract T createItem();

    protected void createPage() {
        VerticalLayout layout = new VerticalLayout();

        if(item != null) {
            createFoundPage(layout);
        } else {
            createNotFoundPage(layout);
        }

        add(layout);
    }


    protected void createFoundPage(VerticalLayout layout) {
        H2 title = new H2(item.getName());

        DescriptionList descriptionList = new DescriptionList();


        DbItemGrid<Book> bookGrid = new BookGrid(bookService, authorService, seriesService, tagService, item).init();


        layout.add(title, descriptionList, bookGrid);
    }

    protected void createNotFoundPage(VerticalLayout layout) {
        Span span = new Span(String.format("id \"%s\" not found", id));
        layout.add(span);
    }


}
