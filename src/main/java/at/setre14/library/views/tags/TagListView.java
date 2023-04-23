package at.setre14.library.views.tags;

import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemListView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Tags")
@Route(value = "tag-list", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class TagListView extends DbItemListView<Tag> {

    public TagListView(TagService service) {
        super(service, Tag.class, TagView.class);
    }
}