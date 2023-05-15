package at.setre14.library.views.series;

import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import javax.annotation.security.RolesAllowed;

@PageTitle("Series")
@Route(value = "series", layout = MainLayout.class)
@AnonymousAllowed
// @RolesAllowed("USER")
@Uses(Icon.class)
public class SeriesView extends DbItemView<Series> {

    public SeriesView(SeriesService service, BookService bookService, AuthorService authorService, TagService tagService) {
        super(service, bookService, authorService, service, tagService);
    }

    @Override
    protected Series createItem() {
        return new Series("New Series");
    }
}
