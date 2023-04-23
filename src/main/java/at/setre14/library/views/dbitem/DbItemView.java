package at.setre14.library.views.dbitem;

import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.dbitem.DbItemService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;

public abstract class DbItemView<T extends DbItem> extends Div implements HasUrlParameter<String> {
    private final DbItemService<T> service;


    protected String id;
    protected T item;

    public DbItemView(DbItemService<T> service) {
        this.service = service;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String id)  {

        if (id == null) {
            this.item = createItem();
            this.id = item.getId();
        } else {
            this.id = id;
            this.item = service.findById(id);
        }

        createPage();
    }

    protected abstract T createItem();

    protected void createPage() {
        VerticalLayout layout = new VerticalLayout();

        if(item != null) {
            createFoundPage(layout);
        } else {
            createNotFoundPage(layout);
        }

        add(layout);
    }

    protected void createFoundPage(VerticalLayout layout) {
        Span span = new Span(item.getName());
        layout.add(span);
    }

    protected void createNotFoundPage(VerticalLayout layout) {
        Span span = new Span(String.format("id \"%s\" not found", id));
        layout.add(span);
    }


}
