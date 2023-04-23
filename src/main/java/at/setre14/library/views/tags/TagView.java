package at.setre14.library.views.tags;

import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Book")
@Route(value = "tag", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class TagView extends DbItemView<Tag> {

    public TagView(TagService service) {
        super(service);
    }

    @Override
    protected Tag createItem() {
        return new Tag("New Tag");
    }
}
