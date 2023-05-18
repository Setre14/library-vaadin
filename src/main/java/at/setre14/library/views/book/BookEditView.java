package at.setre14.library.views.book;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.data.userbooksetting.UserBookSetting;
import at.setre14.library.data.userbooksetting.UserBookSettingService;
import at.setre14.library.model.ReadingStatus;
import at.setre14.library.views.MainLayout;
import at.setre14.library.views.dbitem.DbItemView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Book Edit")
@Route(value = "book-edit", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "USER"})

@Uses(Icon.class)
public class BookEditView extends DbItemView<Book> {

    private final TextField titleTextField = new TextField("Title");
    private final TextArea descriptionTextArea = new TextArea("Description");
    private final ComboBox<Author> authorComboBox = new ComboBox<>("Author");
    private final ComboBox<Series> seriesComboBox = new ComboBox<>("Series");

    private final IntegerField seriesIndexIntegetField = new IntegerField("Series Index");
    private final Checkbox tolinoSyncCheckbox = new Checkbox();
    private final Select<ReadingStatus> readingStatusSelect = new Select<>();
    private final Checkbox physicalCopyCheckbox = new Checkbox();
    private final IntegerField pageIntegerField = new IntegerField("Page");
    //    private final StarsRating starsRating = new StarsRating();
    private final MultiSelectComboBox<Tag> tagsMultiSelectComboBox = new MultiSelectComboBox<>("Tags");
    //    private TextField ebook = new TextField("Email address");
//
    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload ebook = new Upload(buffer);
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final Binder<Book> bookBinder = new Binder<>(Book.class);
    private final Binder<UserBookSetting> userBookSettingBinder = new Binder<>(UserBookSetting.class);


    private List<Author> authors = new ArrayList<>();
    private List<Series> serieses = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    public BookEditView(BookService service, AuthorService authorService, SeriesService seriesService, TagService tagService, UserBookSettingService userBookSettingService) {
        super(service, service, authorService, seriesService, tagService, userBookSettingService);
    }

    @Override
    protected Book createItem() {
        return new Book();
    }

    @Override
    protected void createPage() {
        if (item == null) {
            item = createItem();
        }

        createForm();

//        VerticalLayout layout = new VerticalLayout();
//
//        if (item != null) {
//            createFoundPage(layout);
//        } else {
//            createNotFoundPage(layout);
//        }
//
//        add(layout);
    }

    public void createForm() {
        addClassName("book-edit-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        bookBinder.bind(titleTextField, Book::getName, Book::setName);
        bookBinder.bind(descriptionTextArea, Book::getDescription, Book::setDescription);
        bookBinder.bind(authorComboBox, Book::getAuthor, Book::setAuthor);
        bookBinder.bind(seriesComboBox, Book::getSeries, Book::setSeries);
        bookBinder.bind(seriesIndexIntegetField, Book::getSeriesIndex, Book::setSeriesIndex);
        bookBinder.bind(physicalCopyCheckbox, Book::isPhysical, Book::setPhysical);
        bookBinder.bind(tagsMultiSelectComboBox, Book::getTags, Book::setTags);

        userBookSettingBinder.bind(tolinoSyncCheckbox, UserBookSetting::isSync, UserBookSetting::setSync);
        userBookSettingBinder.bind(readingStatusSelect, UserBookSetting::getStatus, UserBookSetting::setStatus);
        userBookSettingBinder.bind(pageIntegerField, UserBookSetting::getPage, UserBookSetting::setPage);
//        userBookSettingBinder.bind(starsRating, UserBookSetting::getRating, UserBookSetting::setRating);

        bookBinder.setBean(item);
        bookService.addUserBookSettings(item);
        userBookSettingBinder.setBean(item.getUserBookSetting());

        System.out.println(item.getUserBookSetting().getId());


        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
//            personService.update(binder.getBean());
            Notification.show(bookBinder.getBean().getClass().getSimpleName() + " details stored.");
            Book book = bookBinder.getBean();
            UserBookSetting userBookSetting = userBookSettingBinder.getBean();
            userBookSettingService.save(userBookSetting);
            book.updateIds();
            bookService.save(book);
            System.out.println(book);
            UI.getCurrent().navigate(BookView.class, book.getId());
        });
    }

    private void clearForm() {
        bookBinder.setBean(new Book());
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        titleTextField.setAutofocus(true);

        authors = authorService.findAll();
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
        serieses = seriesService.findAll();
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

        tags = tagService.findAll();
        tagsMultiSelectComboBox.setAllowCustomValue(true);
        tagsMultiSelectComboBox.setItems(tags);
        tagsMultiSelectComboBox.setItemLabelGenerator(Tag::getName);
        tagsMultiSelectComboBox.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            Tag customTag = new Tag(customValue);
            tags.add(customTag);
            tagsMultiSelectComboBox.setItems(tags);
            tagsMultiSelectComboBox.setValue(customTag);
        });
//
        seriesIndexIntegetField.setValue(1);
        seriesIndexIntegetField.setStepButtonsVisible(true);
        seriesIndexIntegetField.setMin(1);
//
        pageIntegerField.setValue(0);
        pageIntegerField.setStepButtonsVisible(true);
        pageIntegerField.setMin(0);
//
        readingStatusSelect.setLabel("Reading Status");
        readingStatusSelect.setItems(ReadingStatus.values());
        readingStatusSelect.setValue(ReadingStatus.NOT_READ);
        readingStatusSelect.setItemLabelGenerator(status -> status.name().toLowerCase());
//
        tolinoSyncCheckbox.setLabel("Tolino Sync");
        physicalCopyCheckbox.setLabel("Physical Copy");

//
        ebook.setAutoUpload(false);
        ebook.setAcceptedFileTypes(".epub");
        ebook.setMaxFiles(1);

        ebook.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();

            // Do something with the file data
            // processFile(inputStream, fileName);
        });
        ebook.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

//        Button uploadAllButton = new Button("Upload ebook");
//        uploadAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        uploadAllButton.addClickListener(event -> {
//            // No explicit Flow API for this at the moment
//            ebook.getElement().callJsFunction("uploadFiles");
//        });
//        add(uploadAllButton);


//        add(comboBox);

//        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(
                titleTextField,
                descriptionTextArea,
                authorComboBox,
                seriesComboBox,
                seriesIndexIntegetField,
                readingStatusSelect,
                pageIntegerField,
                tolinoSyncCheckbox,
                physicalCopyCheckbox,
//                starsRating,
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
