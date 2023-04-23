package at.setre14.library.components.grid;

import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.dbitem.DbItemService;
import at.setre14.library.views.dbitem.DbItemView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.function.Function;

public class DbItemGrid<T extends DbItem> extends Div {

    protected Grid<T> grid;
    protected DbItemGridFilter<T> filter;
    protected Paginator paginator;
    protected final DbItemService<T> service;
    private final Class<T> itemClass;
    private final Class<? extends DbItemView<T>> itemViewClass;

    public DbItemGrid(DbItemService<T> service, Class<T> itemClass, Class<? extends DbItemView<T>> itemViewClass) {
        this.service = service;
        this.itemClass = itemClass;
        this.itemViewClass = itemViewClass;
    }


    public DbItemGrid<T> init() {
        setSizeFull();

        this.filter = createFilter();
        this.paginator = new Paginator(this::setGridItems);
        this.grid = createGrid();

        VerticalLayout layout = new VerticalLayout(filter, grid, paginator);
        layout.setSizeFull();

        add(layout);

        return this;
    }

    protected DbItemGridFilter<T> createFilter() {
        return new DbItemGridFilter<T>(this::setGridItems).init();
    }

    protected Grid<T> createGrid() {
        grid = new Grid<>(itemClass, false);
        grid.setWidthFull();
        grid.addColumn(createLinkHeaderComponentRenderer(dbItem -> dbItem, itemViewClass)).setHeader("Name").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        setGridItems();

        return grid;
    }

    protected <K extends DbItem> ComponentRenderer<RouterLink, T> createLinkHeaderComponentRenderer(Function<T, K> dbItemFunc, Class<? extends DbItemView<K>> itemViewClass) {
        return new ComponentRenderer<>(dbItem -> {
            DbItem item = dbItemFunc.apply(dbItem);
            String id = item == null ? "" : item.getId();
            String text = item == null ? "" : item.getName();

            RouterLink link = new RouterLink(text, itemViewClass, id);
            link.addClassName("black-link");

            return link;
        });
    }

    protected void setGridItems() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Page<T> page = service.list(filter, paginator.getPageRequest(order));
        paginator.update(page);

        grid.setItems(page.getContent());
        refreshGrid();

        paginator.validate();
    }

    protected void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
