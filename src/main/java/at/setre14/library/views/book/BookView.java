package at.setre14.library.views.book;

import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.views.DbItemView;
import at.setre14.library.views.MainLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Book")
@Route(value = "book", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class BookView extends DbItemView<Book> {

    public BookView(BookService service) {
        super(service);
    }

    @Override
    protected Book createItem() {
        return new Book("New Book");
    }

    @Override
    protected void createPage() {
        VerticalLayout layout = new VerticalLayout();

        if(item != null) {

            Span span = new Span(item.getName());

            layout.add(span);
        } else {

            Span span = new Span("book id not found");

            layout.add(span);
        }

        add(layout);
    }
}
