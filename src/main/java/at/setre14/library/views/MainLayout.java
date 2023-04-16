package at.setre14.library.views;

import at.setre14.library.components.appnav.AppNav;
import at.setre14.library.components.appnav.AppNavItem;
import at.setre14.library.data.entity.User;
import at.setre14.library.security.AuthenticatedUser;
import at.setre14.library.views.authors.AuthorListView;
import at.setre14.library.views.book.BookEditView;
import at.setre14.library.views.book.BookListView;
import at.setre14.library.views.createuser.CreateUserView;
import at.setre14.library.views.series.SeriesListView;
import at.setre14.library.views.settings.SettingsView;
import at.setre14.library.views.tags.TagListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("library-vaadin");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        if (accessChecker.hasAccess(BookListView.class)) {
            nav.addItem(new AppNavItem("Books", BookListView.class, "la la-book"));

        }
        if (accessChecker.hasAccess(AuthorListView.class)) {
            nav.addItem(new AppNavItem("Authors", AuthorListView.class, "la la-address-book"));

        }
        if (accessChecker.hasAccess(TagListView.class)) {
            nav.addItem(new AppNavItem("Tags", TagListView.class, "la la-tags"));

        }
        if (accessChecker.hasAccess(SeriesListView.class)) {
            nav.addItem(new AppNavItem("Series", SeriesListView.class, "la la-list"));

        }
        if (accessChecker.hasAccess(BookEditView.class)) {
            nav.addItem(new AppNavItem("Book Edit", BookEditView.class, "la la-user"));

        }
        if (accessChecker.hasAccess(CreateUserView.class)) {
            nav.addItem(new AppNavItem("Create User", CreateUserView.class, "la la-user"));

        }
        if (accessChecker.hasAccess(SettingsView.class)) {
            nav.addItem(new AppNavItem("Settings", SettingsView.class, "la la-cog"));

        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
