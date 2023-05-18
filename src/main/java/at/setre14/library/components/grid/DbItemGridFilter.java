package at.setre14.library.components.grid;

import at.setre14.library.data.dbitem.DbItem;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class DbItemGridFilter<T extends DbItem> extends Div {

    protected final TextField nameFilterTextField = new TextField("Name");
    protected final Runnable onSearch;

    public DbItemGridFilter(Runnable onSearch) {
        this.onSearch = onSearch;
    }

    public DbItemGridFilter<T> init() {
        setWidthFull();
        addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                LumoUtility.BoxSizing.BORDER);

        HorizontalLayout filter = createFilter();

        VerticalLayout layout = new VerticalLayout(createFilterExtend(filter), filter);

        add(layout);

        return this;
    }

    private HorizontalLayout createFilterExtend(HorizontalLayout filter) {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
//        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
//                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filter.getClassNames().contains("visible")) {
                filter.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filter.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    protected HorizontalLayout createFilter() {
        HorizontalLayout filters = new HorizontalLayout();
        filters.setWidthFull();
//        filters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
//                LumoUtility.AlignItems.CENTER);
        filters.addClassName("filter-layout");

        nameFilterTextField.setPlaceholder("filter");
        nameFilterTextField.addKeyUpListener(e -> {
            if (Key.ENTER.equals(e.getKey())) {
                onSearch.run();
            }
        });

        // Action buttons
        Button resetBtn = new Button("Reset");
        resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetBtn.addClickListener(e -> {
            nameFilterTextField.clear();
            onSearch.run();
        });
        Button searchBtn = new Button("Search");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e -> onSearch.run());

        Div actions = new Div(resetBtn, searchBtn);
        actions.addClassName(LumoUtility.Gap.SMALL);
        actions.addClassName("actions");

        filters.add(nameFilterTextField, actions);

        return filters;
    }

    public String getNameFilter() {
        return nameFilterTextField.getValue();
    }
}

