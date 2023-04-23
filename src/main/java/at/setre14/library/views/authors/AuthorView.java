package at.setre14.library.views.authors;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Book")
@Route(value = "author", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class AuthorView extends DbItemView<Author> {
    public AuthorView(AuthorService service) {
        super(service);
    }

    @Override
    protected Author createItem() {
        return new Author("New Author");
    }
}
