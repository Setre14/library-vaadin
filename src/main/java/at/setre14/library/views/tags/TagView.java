package at.setre14.library.views.tags;

import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Tags")
@Route(value = "tag", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "USER"})
@Uses(Icon.class)
public class TagView extends DbItemView<Tag> {

    public TagView(TagService service, BookService bookService, AuthorService authorService, SeriesService seriesService) {
        super(service, bookService, authorService, seriesService, service);
    }

    @Override
    protected Tag createItem() {
        return new Tag("New Tag");
    }
}
