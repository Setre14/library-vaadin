package at.setre14.library.components.grid;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.ArrayList;
import java.util.List;

public class BookGridFilter extends DbItemGridFilter<Book> {

    private final ComboBox<Author> authorComboBox = new ComboBox<>();
    private final ComboBox<Series> seriesComboBox = new ComboBox<>();
    private final ComboBox<Tag> tagComboBox = new ComboBox<>();
    private final AuthorService authorService;
    private final SeriesService seriesService;
    private final TagService tagService;
    private DbItem item = null;


    public BookGridFilter(AuthorService authorService, SeriesService seriesService, TagService tagService, Runnable onSearch) {
        super(onSearch);
        this.authorService = authorService;
        this.seriesService = seriesService;
        this.tagService = tagService;
    }

    public BookGridFilter(AuthorService authorService, SeriesService seriesService, TagService tagService, DbItem item, Runnable onSearch) {
        this(authorService, seriesService, tagService, onSearch);
        this.item = item;
    }

    @Override
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

        String itemClassName = item != null ? item.getClass().getSimpleName() : "no-item";

        List<Component> filterComboBoxes = new ArrayList<>();

        if (!itemClassName.equals("Author")) {
            authorComboBox.setLabel("Author");
            authorComboBox.setPlaceholder("Select Author");
            authorComboBox.setItems(authorService.findAll());
            filterComboBoxes.add(authorComboBox);
        }

        if (!itemClassName.equals("Series")) {
            seriesComboBox.setLabel("Series");
            seriesComboBox.setPlaceholder("Select Series");
            seriesComboBox.setItems(seriesService.findAll());
            filterComboBoxes.add(seriesComboBox);
        }

        if (!itemClassName.equals("Tag")) {
            tagComboBox.setLabel("Tag");
            tagComboBox.setPlaceholder("Select Tag");
            tagComboBox.setItems(tagService.findAll());
            filterComboBoxes.add(tagComboBox);
        }

        // Action buttons
        Button resetBtn = new Button("Reset");
        resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetBtn.addClickListener(e -> {
            nameFilterTextField.clear();
            authorComboBox.clear();
            seriesComboBox.clear();
            tagComboBox.clear();
            onSearch.run();
        });
        Button searchBtn = new Button("Search");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e -> onSearch.run());

        Div actions = new Div(resetBtn, searchBtn);
        actions.addClassName(LumoUtility.Gap.SMALL);
        actions.addClassName("actions");

        filters.add(nameFilterTextField);
        filters.add(filterComboBoxes);
        filters.add(actions);

        return filters;
    }

    public Author getAuthorFilter() {
        return item != null && item.getClass().getSimpleName().equals("Author") ? (Author) item : authorComboBox.getValue();
    }

    public Series getSeriesFilter() {
        return item != null && item.getClass().getSimpleName().equals("Series") ? (Series) item : seriesComboBox.getValue();
    }

    public Tag getTagFilter() {
        return item != null && item.getClass().getSimpleName().equals("Tag") ? (Tag) item : tagComboBox.getValue();
    }
}

