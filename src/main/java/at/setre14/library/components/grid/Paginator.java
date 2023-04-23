package at.setre14.library.components.grid;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class Paginator extends Div {
    private int pageNumber = 1;
    private int pagesTotal = 1;
    private final IntegerField pageNumberIntegerField = new IntegerField();

    private final Span pagesTotalSpan = new Span("1");

    private final Runnable onUpdate;

    public Paginator(Runnable onUpdate) {
        this.onUpdate = onUpdate;
        HorizontalLayout horizontalLayout = new HorizontalLayout();

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
                onUpdate.run();
            }
        });

        Button prevPageButton = new Button("<");
        prevPageButton.addClickListener(e -> {
            if(pageNumber > 1) {
                pageNumber--;
                pageNumberIntegerField.setValue(pageNumber);
                onUpdate.run();
            }
        });

        pagesTotalSpan.setText(String.valueOf(pagesTotal));

        Button nextPageButton = new Button(">");
        nextPageButton.addClickListener(e -> {
            if(pageNumber < pagesTotal) {
                pageNumber++;
                pageNumberIntegerField.setValue(pageNumber);
                onUpdate.run();
            }
        });

        horizontalLayout.add(prevPageButton, pageNumberIntegerField, pagesTotalSpan, nextPageButton);

        add(horizontalLayout);
    }

    public PageRequest getPageRequest(Sort.Order order) {
        int pageSize = 20;
        return PageRequest.of(pageNumber-1, pageSize, Sort.by(order));
    }

    public <T> void update(Page<T> page) {
        pagesTotal = page.getTotalPages();
        if (pagesTotal == 0) {
            pagesTotal = 1;
        }
        pagesTotalSpan.setText(String.valueOf(pagesTotal));
        pageNumberIntegerField.setMax(pagesTotal);
    }

    public void validate() {
        if(pageNumber > pagesTotal) {
            pageNumber = pagesTotal;
            pageNumberIntegerField.setValue(pageNumber);
            onUpdate.run();
        }
    }
}
