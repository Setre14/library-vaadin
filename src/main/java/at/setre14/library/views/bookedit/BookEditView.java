package at.setre14.library.views.bookedit;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.service.SamplePersonService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.model.*;
import at.setre14.library.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PageTitle("Book Edit")
@Route(value = "book-edit", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class BookEditView extends Div {

    private final TextField titleTextField = new TextField("Title");
    private final TextArea descriptionTextArea = new TextArea("Description");
    private final ComboBox<Author> authorComboBox = new ComboBox<>("Author");
    private final ComboBox<Series> seriesComboBox = new ComboBox<>("Series");

    private final IntegerField seriesIndexIntegetField = new IntegerField("Series Index");
    private final Checkbox tolinoSyncCheckbox = new Checkbox();
    private final Select<ReadingStatus> readingStatusSelect = new Select<>();
    private final Checkbox physicalCopyCheckbox = new Checkbox();
    private final IntegerField pageIntegerField = new IntegerField("Page");
    private final TextField ratingTextfield = new TextField("Rating");
    private final MultiSelectComboBox<Tag> tagsMultiSelectComboBox = new MultiSelectComboBox<>("Tags");
//    private TextField ebook = new TextField("Email address");
//
    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload ebook = new Upload(buffer);
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final Binder<Book> binder = new Binder<>(Book.class);


    private final List<Author> authors = new ArrayList<>(
            Arrays.asList(
                    new Author("Chrome"),
                    new Author("Edge"),
                    new Author("Firefox"),
                    new Author("Safari")
            )
    );
    private final List<Series> serieses = new ArrayList<>(
            Arrays.asList(
                    new Series("Chrome"),
                    new Series("Edge"),
                    new Series("Firefox"),
                    new Series("Safari")
            )
    );

    public BookEditView(SamplePersonService personService) {
        addClassName("book-edit-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        binder.bind(titleTextField, Book::getName, Book::setName);
        binder.bind(descriptionTextArea, Book::getDescription, Book::setDescription);
        binder.bind(authorComboBox, Book::getAuthor, Book::setAuthor);
        binder.bind(seriesComboBox, Book::getSeries, Book::setSeries);
        binder.bind(seriesIndexIntegetField, Book::getSeriesIndex, Book::setSeriesIndex);
//        binder.bind(tolinoSyncCheckbox, Book::getName, Book::setName);
        binder.bind(physicalCopyCheckbox, Book::isPhysical, Book::setPhysical);
        binder.bind(tagsMultiSelectComboBox, Book::getTags, Book::setTags);


//        private final TextField titleTextField = new TextField("Title");
//        private final TextArea descriptionTextArea = new TextArea("Description");
//        private final ComboBox<Author> authorComboBox = new ComboBox<>("Author");
//        private final ComboBox<Series> seriesComboBox = new ComboBox<>("Series");
//
//        private final IntegerField seriesIndexIntegetField = new IntegerField("Series Index");
//        private final Checkbox tolinoSyncCheckbox = new Checkbox();
//        private final Select<ReadingStatus> readingStatusSelect = new Select<>();
//        private final Checkbox physicalCopyCheckbox = new Checkbox();
//        private final IntegerField pageIntegerField = new IntegerField("Page");
//        private final TextField ratingTextfield = new TextField("Rating");
//        private final MultiSelectComboBox<String> tagsMultiSelectComboBox = new MultiSelectComboBox<>("Tags");
////    private TextField ebook = new TextField("Email address");
//
//        private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
//        private final Upload ebook = new Upload(buffer);


//        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
//            personService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
            Book book = binder.getBean();
            System.out.println(book.toString());
            clearForm();
        });
    }

    private void clearForm() {
        binder.setBean(new Book());
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        titleTextField.setAutofocus(true);

        authorComboBox.setAllowCustomValue(true);
        authorComboBox.setItems(authors);
        authorComboBox.setItemLabelGenerator(Author::getName);

        authorComboBox.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            Author customAuthor = new Author(customValue);
            authors.add(customAuthor);
            authorComboBox.setItems(authors);
            authorComboBox.setValue(customAuthor);
        });
//
        seriesComboBox.setAllowCustomValue(true);
        seriesComboBox.setItems(serieses);
        seriesComboBox.setItemLabelGenerator(Series::getName);
        seriesComboBox.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            Series customSeries = new Series(customValue);
            serieses.add(customSeries);
            seriesComboBox.setItems(serieses);
            seriesComboBox.setValue(customSeries);
        });
//
        seriesIndexIntegetField.setValue(1);
        seriesIndexIntegetField.setStepButtonsVisible(true);
        seriesIndexIntegetField.setMin(1);
//
//        pageIntegerField.setValue(0);
//        pageIntegerField.setStepButtonsVisible(true);
//        pageIntegerField.setMin(0);
//
//        readingStatusSelect.setLabel("Reading Status");
//        readingStatusSelect.setItems(ReadingStatus.values());
//        readingStatusSelect.setValue(ReadingStatus.NOT_READ);
//        readingStatusSelect.setItemLabelGenerator(status -> status.name().toLowerCase());
//
//        tolinoSyncCheckbox.setLabel("Tolino Sync");
        physicalCopyCheckbox.setLabel("Physical Copy");
//
        ebook.setAutoUpload(false);
        ebook.setAcceptedFileTypes(".epub");
        ebook.setMaxFiles(1);

        ebook.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();

            System.out.println(fileName);

            // Do something with the file data
            // processFile(inputStream, fileName);
        });
        ebook.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        Button uploadAllButton = new Button("Upload ebook");
        uploadAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        uploadAllButton.addClickListener(event -> {
            // No explicit Flow API for this at the moment
            ebook.getElement().callJsFunction("uploadFiles");
        });
        add(uploadAllButton);


//        add(comboBox);

//        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(
                titleTextField,
                descriptionTextArea,
                authorComboBox,
                seriesComboBox,
                seriesIndexIntegetField,
//                readingStatusSelect,
//                pageIntegerField,
//                tolinoSyncCheckbox,
                physicalCopyCheckbox,
//                ratingTextfield,
                tagsMultiSelectComboBox,
                ebook
        );

//        formLayout.addFormItem(tolinoSync, "Tolino Sync");

        formLayout.setColspan(titleTextField, 2);
        formLayout.setColspan(descriptionTextArea, 2);
        formLayout.setColspan(authorComboBox, 2);
//        formLayout.setColspan(ratingTextfield, 2);
        formLayout.setColspan(tagsMultiSelectComboBox, 2);
        formLayout.setColspan(ebook, 2);

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

    private static class PhoneNumberField extends CustomField<String> {
        private final ComboBox<String> countryCode = new ComboBox<>();
        private final TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setAllowedCharPattern("[\\+\\d]");
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setAllowedCharPattern("\\d");
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

}
