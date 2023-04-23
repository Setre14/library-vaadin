package at.setre14.library.views.book;

import at.setre14.library.components.grid.BookGrid;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.components.grid.DbItemGrid;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.RolesAllowed;

@PageTitle("Books")
@Route(value = "book-list", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class BookListView extends Div {

    public BookListView(BookService bookService, AuthorService authorService, SeriesService seriesService, TagService tagService) {
        setSizeFull();

        DbItemGrid<Book> grid = new BookGrid(bookService, authorService, seriesService, tagService).init();
        VerticalLayout layout = new VerticalLayout(grid);
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }
}
