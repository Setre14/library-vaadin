package at.setre14.library.components.dbitemlink;

import com.vaadin.flow.router.RouterLink;

public class DbItemLink extends RouterLink {

    public DbItemLink(String text, Class destClass, String param) {
        super(text, destClass, param);
        addClassName("black-link");
    }
}
