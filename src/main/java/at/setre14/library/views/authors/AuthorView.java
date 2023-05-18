package at.setre14.library.views.authors;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.data.userbooksetting.UserBookSettingService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Author")
@Route(value = "author", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "USER"})
@Uses(Icon.class)
public class AuthorView extends DbItemView<Author> {
    public AuthorView(AuthorService service, BookService bookService, SeriesService seriesService, TagService tagService, UserBookSettingService userBookSettingService) {
        super(service, bookService, service, seriesService, tagService, userBookSettingService);
    }

    @Override
    protected Author createItem() {
        return new Author("New Author");
    }
}
