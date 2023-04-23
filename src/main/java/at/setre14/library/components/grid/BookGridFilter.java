package at.setre14.library.components.grid;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.tag.Tag;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

public class BookGridFilter extends DbItemGridFilter<Book> {

    private final ComboBox<Author> authorComboBox = new ComboBox<>();
    private final ComboBox<Series> seriesComboBox = new ComboBox<>();
    private final ComboBox<Tag> tagComboBox = new ComboBox<>();
    private final List<Author> authors;
    private final List<Series> series;
    private final List<Tag> tags;

    public BookGridFilter(List<Author> authors, List<Series> series, List<Tag> tags, Runnable onSearch) {
        super(onSearch);
        this.authors = authors;
        this.series = series;
        this.tags = tags;
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
            if(Key.ENTER.equals(e.getKey())) {
                onSearch.run();
            }
        });

        authorComboBox.setLabel("Author");
        authorComboBox.setPlaceholder("Select Author");
        authorComboBox.setItems(authors);

        seriesComboBox.setLabel("Series");
        seriesComboBox.setPlaceholder("Select Series");
        seriesComboBox.setItems(series);

        tagComboBox.setLabel("Tag");
        tagComboBox.setPlaceholder("Select Tag");
        tagComboBox.setItems(tags);

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

        filters.add(nameFilterTextField, authorComboBox, seriesComboBox, tagComboBox, actions);

        return filters;
    }

    public Author getAuthorFilter() {
        return authorComboBox.getValue();
    }
    public Series getSeriesFilter() {
        return seriesComboBox.getValue();
    }
    public Tag getTagFilter() {
        return tagComboBox.getValue();
    }

}

