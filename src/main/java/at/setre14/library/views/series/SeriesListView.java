package at.setre14.library.views.series;

import at.setre14.library.data.series.Series;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemListView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Series")
@Route(value = "series-list", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class SeriesListView extends DbItemListView<Series> {

    public SeriesListView(SeriesService service) {
        super(service, Series.class, SeriesView.class);
    }
}