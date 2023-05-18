package at.setre14.library.views.createuser;

import at.setre14.library.data.Role;
import at.setre14.library.data.user.User;
import at.setre14.library.data.user.UserService;
import at.setre14.library.security.AuthenticatedUser;
import at.setre14.library.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Create User")
@Route(value = "create-user", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class CreateUserView extends Div implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;
    private final UserService userService;

    private final TextField usernameTextField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");
    private final ComboBox<Role> rolesComboBox = new ComboBox<>("Roles");

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final Binder<User> binder = new Binder<>(User.class);

    public CreateUserView(AuthenticatedUser authenticatedUser, UserService userService) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;

        addClassName("create-user-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

//        binder.bindInstanceFields(this);
        binder.bind(usernameTextField, User::getName, User::setName);
        binder.bind(passwordField, User::getPassword, User::setPassword);
        binder.bind(rolesComboBox, User::getRole, User::setRole);
//        binder.bind(username, User::getName, Book::setName);

        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            System.out.println(binder.getBean());
//            personService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
            userService.save(binder.getBean());
            clearForm();
        });
    }

    private void clearForm() {
        binder.setBean(new User());
    }

    private Component createTitle() {
        return new H3("Create user");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        rolesComboBox.setItems(Role.values());
        rolesComboBox.setItemLabelGenerator(Role::getName);

//        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(usernameTextField, passwordField, rolesComboBox);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        User user = authenticatedUser.get();
        if (user != null) {
            if (user.getRole() != Role.ADMIN) {
                beforeEnterEvent.forwardTo("");
            }
        } else if (userService.hasAdminUsers()) {
            beforeEnterEvent.forwardTo("/login");
        }
    }
}
