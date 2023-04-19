package at.setre14.library.views.book;

import at.setre14.library.components.dbitemlink.DbItemLink;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.views.DbItemView;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.authors.AuthorView;
import at.setre14.library.views.series.SeriesView;
import at.setre14.library.views.tags.TagView;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.DescriptionList;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Book")
@Route(value = "book", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class BookView extends DbItemView<Book> {

    public BookView(BookService service) {
        super(service);
    }

    @Override
    protected Book createItem() {
        return new Book("New Book");
    }

    @Override
    protected void createPage() {
        VerticalLayout layout = new VerticalLayout();

        if(item != null) {
            H2 title = new H2(item.getName());

            DescriptionList descriptionList = new DescriptionList();

            createRow(descriptionList, "Description", String.valueOf(item.getDescription()));

            createLinkRow(descriptionList, "Author", item.getAuthor(), AuthorView.class);
            if(item.getSeries() != null) {
                createLinkRow(descriptionList, "Series", item.getSeries(), SeriesView.class);
                createRow(descriptionList, "Series Index", String.valueOf(item.getSeriesIndex()));
            }


            DescriptionList.Term tagsTerm = new DescriptionList.Term("Tags");

            Span tags = new Span();
            for (Tag tag : item.getSortedTags()) {
                DbItemLink link = new DbItemLink(tag.toString(), TagView.class, tag.getId());
                link.getElement().getThemeList().add("badge contrast");
                tags.add(link);
            }
            DescriptionList.Description description = new DescriptionList.Description(tags);
            descriptionList.add(tagsTerm, description);


            add(title, descriptionList);
        } else {

            Span span = new Span(String.format("book id \"%s\" not found", id));

            layout.add(span);
        }

        add(layout);
    }

    protected void createRow(DescriptionList descriptionList, String label, String value) {
        DescriptionList.Term term = new DescriptionList.Term(label);
        DescriptionList.Description description = new DescriptionList.Description(value);

        descriptionList.add(term, description);
    }

    protected void createLinkRow(DescriptionList descriptionList, String label, DbItem item, Class destClass) {
        DescriptionList.Term term = new DescriptionList.Term(label);
        DescriptionList.Description description = new DescriptionList.Description(new DbItemLink(item.getName(), destClass, item.getId()));

        descriptionList.add(term, description);
    }
}
