package at.setre14.library.views.book;

import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.entity.SamplePerson;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.authors.AuthorView;
import at.setre14.library.views.series.SeriesView;
import at.setre14.library.views.tags.TagView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.security.RolesAllowed;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@PageTitle("Books")
@Route(value = "book-list", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class BookListView extends Div {

    private Grid<Book> grid;

    private int pageNumber = 1;
    private final int pageSize = 20;
    private int pagesTotal = 1;
    private final Filters filters;
    private final BookService bookService;

    public BookListView(BookService bookService) {
        this.bookService = bookService;
        setSizeFull();
        addClassNames("book-list-view");

        filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid(), createPaging());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }


    private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    public static class Filters extends Div implements Specification<SamplePerson> {

        private final TextField name = new TextField("Title");
        private final TextField phone = new TextField("Phone");
        private final DatePicker startDate = new DatePicker("Date of Birth");
        private final DatePicker endDate = new DatePicker();
        private final MultiSelectComboBox<String> occupations = new MultiSelectComboBox<>("Occupation");
        private final CheckboxGroup<String> roles = new CheckboxGroup<>("Role");

        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            name.setPlaceholder("First or last name");

            occupations.setItems("Insurance Clerk", "Mortarman", "Beer Coil Cleaner", "Scale Attendant");

            roles.setItems("Worker", "Supervisor", "Manager", "External");
            roles.addClassName("double-width");

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                name.clear();
                phone.clear();
                startDate.clear();
                endDate.clear();
                occupations.clear();
                roles.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(name, phone, createDateRangeFilter(), occupations, roles, actions);
        }

        private Component createDateRangeFilter() {
            startDate.setPlaceholder("From");

            endDate.setPlaceholder("To");

            // For screen readers
            setAriaLabel(startDate, "From date");
            setAriaLabel(endDate, "To date");

            FlexLayout dateRangeComponent = new FlexLayout(startDate, new Text(" – "), endDate);
            dateRangeComponent.setAlignItems(FlexComponent.Alignment.BASELINE);
            dateRangeComponent.addClassName(LumoUtility.Gap.XSMALL);

            return dateRangeComponent;
        }

        private void setAriaLabel(DatePicker datePicker, String label) {
            datePicker.getElement().executeJs("const input = this.inputElement;" //
                    + "input.setAttribute('aria-label', $0);" //
                    + "input.removeAttribute('aria-labelledby');", label);
        }

        @Override
        public Predicate toPredicate(Root<SamplePerson> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!name.isEmpty()) {
                String lowerCaseFilter = name.getValue().toLowerCase();
                Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                        lowerCaseFilter + "%");
                Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                        lowerCaseFilter + "%");
                predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
            }
            if (!phone.isEmpty()) {
                String databaseColumn = "phone";
                String ignore = "- ()";

                String lowerCaseFilter = ignoreCharacters(ignore, phone.getValue().toLowerCase());
                Predicate phoneMatch = criteriaBuilder.like(
                        ignoreCharacters(ignore, criteriaBuilder, criteriaBuilder.lower(root.get(databaseColumn))),
                        "%" + lowerCaseFilter + "%");
                predicates.add(phoneMatch);

            }
            if (startDate.getValue() != null) {
                String databaseColumn = "dateOfBirth";
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(databaseColumn),
                        criteriaBuilder.literal(startDate.getValue())));
            }
            if (endDate.getValue() != null) {
                String databaseColumn = "dateOfBirth";
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.literal(endDate.getValue()),
                        root.get(databaseColumn)));
            }
            if (!occupations.isEmpty()) {
                String databaseColumn = "occupation";
                List<Predicate> occupationPredicates = new ArrayList<>();
                for (String occupation : occupations.getValue()) {
                    occupationPredicates
                            .add(criteriaBuilder.equal(criteriaBuilder.literal(occupation), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(occupationPredicates.toArray(Predicate[]::new)));
            }
            if (!roles.isEmpty()) {
                String databaseColumn = "role";
                List<Predicate> rolePredicates = new ArrayList<>();
                for (String role : roles.getValue()) {
                    rolePredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(role), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(rolePredicates.toArray(Predicate[]::new)));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }

        private String ignoreCharacters(String characters, String in) {
            String result = in;
            for (int i = 0; i < characters.length(); i++) {
                result = result.replace(String.valueOf(characters.charAt(i)), "");
            }
            return result;
        }

        private Expression<String> ignoreCharacters(String characters, CriteriaBuilder criteriaBuilder,
                Expression<String> inExpression) {
            Expression<String> expression = inExpression;
            for (int i = 0; i < characters.length(); i++) {
                expression = criteriaBuilder.function("replace", String.class, expression,
                        criteriaBuilder.literal(characters.charAt(i)), criteriaBuilder.literal(""));
            }
            return expression;
        }

    }

    private Component createGrid() {
        grid = new Grid<>(Book.class, false);
        grid.addColumn(createLinkHeaderComponentRenderer(BookView.class, book -> book)).setHeader("Title").setAutoWidth(true);
        grid.addColumn(createLinkHeaderComponentRenderer(AuthorView.class, Book::getAuthor)).setHeader("Author").setAutoWidth(true);
        grid.addColumn(createLinkHeaderComponentRenderer(SeriesView.class, Book::getSeries)).setHeader("Series").setAutoWidth(true);
        grid.addColumn(createTagsComponentRenderer()).setHeader("Tags").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        setGridItems();

        return grid;
    }

    private HorizontalLayout createPaging() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        IntegerField pageNumberIntegerField = new IntegerField();
        pageNumberIntegerField.setMin(1);
        pageNumberIntegerField.setMax(pagesTotal);
        pageNumberIntegerField.setValue(pageNumber);

        pageNumberIntegerField.addKeyUpListener(e -> {
            if(Key.ENTER.equals(e.getKey())) {
                pageNumber = pageNumberIntegerField.getValue();
                if(pageNumber < 1) {
                    pageNumber = 1;
                } else if (pageNumber > pagesTotal) {
                    pageNumber = pagesTotal;
                }
                pageNumberIntegerField.setValue(pageNumber);
                setGridItems();
            }
        });

        Button prevPageButton = new Button("<");
        prevPageButton.addClickListener(e -> {
            if(pageNumber > 1) {
                pageNumber--;
                pageNumberIntegerField.setValue(pageNumber);
                setGridItems();
            }
        });



        Span pagesTotalSpan = new Span(String.valueOf(pagesTotal));

        Button nextPageButton = new Button(">");
        nextPageButton.addClickListener(e -> {
            pageNumber++;
            pageNumberIntegerField.setValue(pageNumber);
            setGridItems();
        });

        horizontalLayout.add(prevPageButton, pageNumberIntegerField, pagesTotalSpan, nextPageButton);

        return horizontalLayout;
    }

//    private ComponentRenderer<RouterLink, Book> createTagsComponentRenderer() {
//        return new ComponentRenderer<>(book -> {
//            HorizontalLayout horizontalLayout = new HorizontalLayout();
//            for(Tag tag: book.getSortedTags()) {
//                Span tagSpan = new Span(tag.toString());
//                tagSpan.getElement().getThemeList().add("badge");
//
//                RouterLink link = new RouterLink(tag.toString(), SettingsView.class);
//                link.getElement().getThemeList().add("badge");
//
//                horizontalLayout.add(link);
//            }
//            return horizontalLayout;
//        });
//    }

    private ComponentRenderer<RouterLink, Book> createLinkHeaderComponentRenderer(Class viewClass, Function<Book, DbItem> dbItemFunc) {
        return new ComponentRenderer<>(book -> {
            DbItem item = dbItemFunc.apply(book);
            String id = item == null ? "" : item.getId();
            String text = item == null ? "" : item.getName();

            RouterLink link = new RouterLink(text, viewClass, id);

            link.addClassName("grid-cell-link");

            return link;
        });
    }
    private ComponentRenderer<HorizontalLayout, Book> createTagsComponentRenderer() {
        return new ComponentRenderer<>(book -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            for(Tag tag: book.getSortedTags()) {
                RouterLink link = new RouterLink(tag.toString(), TagView.class, tag.getId());
                link.getElement().getThemeList().add("badge contrast");
                link.addClassName("grid-cell-link");

                horizontalLayout.add(link);
            }
            return horizontalLayout;
        });
    }

    private void setGridItems() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Page<Book> page = bookService.list(PageRequest.of(pageNumber, pageSize, Sort.by(order)));
        pagesTotal = page.getTotalPages() - 1;

        grid.setItems(page.getContent());
        refreshGrid();
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
