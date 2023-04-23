package at.setre14.library.views.dbitem;

import at.setre14.library.components.grid.DbItemGrid;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.dbitem.DbItemService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class DbItemListView<T extends DbItem> extends Div {
    private final DbItemService<T> service;

    public DbItemListView(DbItemService<T> service, Class<T> itemClass, Class<? extends DbItemView<T>> itemViewClass) {
        this.service = service;

        createPage(itemClass, itemViewClass);
    }

    protected void createPage(Class<T> itemClass, Class<? extends DbItemView<T>> itemViewClass) {
        setSizeFull();

        VerticalLayout layout = new VerticalLayout();

        layout.add(new DbItemGrid<>(service, itemClass, itemViewClass).init());

        add(layout);
    }


}
