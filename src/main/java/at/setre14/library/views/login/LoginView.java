package at.setre14.library.views.login;

import at.setre14.library.security.AuthenticatedUser;
import at.setre14.library.views.MainLayout;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "login", layout = MainLayout.class)
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final AuthenticatedUser authenticatedUser;

    private final LoginForm login = new LoginForm();

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");

        add(login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
//        if(beforeEnterEvent.getLocation()
//                .getQueryParameters()
//                .getParameters()
//                .containsKey("error")) {
//            login.setError(true);
//        }
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
//            setOpened(false);
            beforeEnterEvent.forwardTo("");
        }

//        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}