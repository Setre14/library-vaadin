package at.setre14.library.views.authors;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemListView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Authors")
@Route(value = "author-list", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class AuthorListView extends DbItemListView<Author> {
    public AuthorListView(AuthorService authorService) {
        super(authorService, Author.class, AuthorView.class);
    }
}
