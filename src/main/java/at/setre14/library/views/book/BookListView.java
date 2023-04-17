package at.setre14.library.views.book;

import at.setre14.library.data.book.BookService;
import at.setre14.library.views.MainLayout;
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

    public BookListView(BookService bookService) {
        setSizeFull();

        BookListGrid grid = new BookListGrid(bookService);
        VerticalLayout layout = new VerticalLayout(grid);
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

}
