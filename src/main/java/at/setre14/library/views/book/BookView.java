package at.setre14.library.views.book;

import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.data.userbooksetting.UserBookSetting;
import at.setre14.library.data.userbooksetting.UserBookSettingService;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.authors.AuthorView;
import at.setre14.library.views.dbitem.DbItemView;
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
import com.vaadin.flow.router.RouterLink;

import javax.annotation.security.RolesAllowed;

@PageTitle("Book")
@Route(value = "book", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "USER"})
@Uses(Icon.class)
public class BookView extends DbItemView<Book> {

    public BookView(BookService service, AuthorService authorService, SeriesService seriesService, TagService tagService, UserBookSettingService userBookSettingService) {
        super(service, service, authorService, seriesService, tagService, userBookSettingService);
    }

    @Override
    protected Book createItem() {
        return new Book("New Book");
    }

    @Override
    protected void createFoundPage(VerticalLayout layout) {
        H2 title = new H2(item.getName());

        DescriptionList descriptionList = new DescriptionList();

        createRow(descriptionList, "Description", String.valueOf(item.getDescription()));

        createLinkRow(descriptionList, "Author", item.getAuthor(), AuthorView.class);
        if (item.getSeries() != null) {
            createLinkRow(descriptionList, "Series", item.getSeries(), SeriesView.class);
            createRow(descriptionList, "Series Index", String.valueOf(item.getSeriesIndex()));
        }


        DescriptionList.Term tagsTerm = new DescriptionList.Term("Tags");

        Span tags = new Span();
        for (Tag tag : item.getSortedTags()) {
            RouterLink link = new RouterLink(tag.toString(), TagView.class, tag.getId());
            link.addClassName("black-link");
            link.getElement().getThemeList().add("badge contrast");
            tags.add(link);
        }
        DescriptionList.Description description = new DescriptionList.Description(tags);
        descriptionList.add(tagsTerm, description);

        bookService.addUserBookSettings(item);
        UserBookSetting userBookSetting = item.getUserBookSetting();

        createRow(descriptionList, "Physical", String.valueOf(item.isPhysical()));
        createRow(descriptionList, "Status", String.valueOf(userBookSetting.getStatus()));
        createRow(descriptionList, "Page", String.valueOf(userBookSetting.getPage()));
        createRow(descriptionList, "Rating", String.valueOf(userBookSetting.getRating()));
        System.out.println(userBookSetting.getId());
        System.out.println(userBookSetting.isSync());
        createRow(descriptionList, "Tolino Sync", String.valueOf(userBookSetting.isSync()));

        RouterLink link = new RouterLink("Edit", BookEditView.class, id);


        add(title, descriptionList, link);

        System.out.println(item.getUserBookSetting());
    }

    protected void createRow(DescriptionList descriptionList, String label, String value) {
        DescriptionList.Term term = new DescriptionList.Term(label);
        DescriptionList.Description description = new DescriptionList.Description(value);

        descriptionList.add(term, description);
    }

    protected void createLinkRow(DescriptionList descriptionList, String label, DbItem item, Class destClass) {
        DescriptionList.Term term = new DescriptionList.Term(label);

        RouterLink link = new RouterLink(item.getName(), destClass, item.getId());
        link.addClassName("black-link");

        DescriptionList.Description description = new DescriptionList.Description(link);

        descriptionList.add(term, description);
    }
}
