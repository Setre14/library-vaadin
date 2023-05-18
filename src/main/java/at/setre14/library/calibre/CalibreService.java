package at.setre14.library.calibre;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.author.AuthorService;
import at.setre14.library.data.book.Book;
import at.setre14.library.data.book.BookService;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.series.SeriesService;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.tag.TagService;
import at.setre14.library.data.user.User;
import at.setre14.library.data.user.UserService;
import at.setre14.library.data.userbooksetting.UserBookSetting;
import at.setre14.library.data.userbooksetting.UserBookSettingService;
import at.setre14.library.model.Language;
import at.setre14.library.model.ReadingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.entry;

@Service
@Slf4j
public class CalibreService {

    private static final String METADATA_FILE = "metadata.opf";
    private static final Map<String, String> DESCRIPTION_TAGS = Map.ofEntries(
            entry("<div>", ""),
            entry("</div>", "\n"),
            entry("<br>", "\n"),
            entry("<p>", ""),
            entry("<p align=\"justify\">", ""),
            entry("</p>", "\n")
    );
    private static final String VALUE_FIELD = "#value#";
    private static final String DISPLAY_FIELD = "display";
    private static final String DEFAULT_VALUE_FIELD = "default_value";
    private final AuthorService authorService;
    private final BookService bookService;
    private final SeriesService seriesService;
    private final TagService tagService;
    private final UserBookSettingService userBookSettingService;
    private final UserService userService;

    public CalibreService(AuthorService authorService, BookService bookService, SeriesService seriesService, TagService tagService, UserBookSettingService userBookSettingService, UserService userService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.seriesService = seriesService;
        this.tagService = tagService;
        this.userBookSettingService = userBookSettingService;
        this.userService = userService;
    }

    private static <T extends DbItem> Map<String, T> createLookupMap(List<T> list, Function<T, String> func) {
        return list.stream().collect(Collectors.toMap(func, Function.identity()));
    }

    private static Set<File> listDirs(String path) {
        return listDirs(new File(path));
    }

    private static Set<File> listDirs(@NonNull File path) {
        return Arrays.stream(path.listFiles())
                .filter(File::isDirectory)
                .collect(Collectors.toSet());
    }

    private static Set<File> listFiles(File path) {
        return Arrays.stream(path.listFiles())
                .filter(file -> !file.isDirectory())
                .collect(Collectors.toSet());
    }

    private static NodeList getElements(Document doc, CalibreTag tag) {
        return doc.getElementsByTagName(tag.tag);
    }

    private static String getElement(Document doc, CalibreTag tag) {
        Node element = getElements(doc, tag).item(0);
        return element != null ? element.getTextContent() : null;
    }

    private static String getNamedItem(NamedNodeMap attributes, CalibreAttribute attribute) {
        return attributes.getNamedItem(attribute.attribute).getTextContent();
    }

    private static UserBookSetting getUserBookSetting(Map<String, UserBookSetting> userBookSettingMap, String userId, String bookId) {
        UserBookSetting userBookSetting = userBookSettingMap.get(userId);

        if (userBookSetting == null) {
            userBookSetting = new UserBookSetting(userId, bookId);
            userBookSettingMap.put(userId, userBookSetting);
        }

        return userBookSetting;
    }

    private static String getCustomValue(String content, String defaultValue) {
        String returnValue = defaultValue;

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> map = mapper.readValue(content, Map.class);

            String value = getMapValue(map, VALUE_FIELD);
            if (value != null) {
                returnValue = value;
            } else {
                Map<String, Object> displayMap = (Map<String, Object>) map.get(DISPLAY_FIELD);
                if (displayMap != null) {
                    String val = getMapValue(displayMap, DEFAULT_VALUE_FIELD);
                    if (val != null) {
                        returnValue = val;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    private static String getMapValue(Map<String, Object> map, String field) {
        String value = null;
        if (map.containsKey(field)) {
            Object val = map.get(field);
            if (val != null) {
                value = val.toString();
                if (value.equals("null")) {
                    value = null;
                }
            }
        }

        return value;
    }

    public CalibreImport importDb(String path) throws Exception {

        List<Author> initAuthors = authorService.findAll();
        List<Book> initBooks = bookService.findAll();
        List<Series> initSeries = seriesService.findAll();
        List<Tag> initTags = tagService.findAll();
        List<User> users = userService.findAll();

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

        String tagName = "import-" + time.format(formatter);
        Tag importTag = new Tag(tagName);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();

        CalibreImport calibreImport = new CalibreImport();

        Map<String, Author> authorMap = createLookupMap(initAuthors, Author::getName);
        Map<String, Book> bookMap = createLookupMap(initBooks, Book::createMapKey);
        Map<String, Series> seriesMap = createLookupMap(initSeries, Series::getName);
        Map<String, Tag> tagsMap = createLookupMap(initTags, Tag::getName);
        Map<String, User> userMap = createLookupMap(users, User::getName);

        User simon = userMap.containsKey("setre14") ? userMap.get("setre14") : new User("simon", "simon");
        User tamara = userMap.containsKey("tamara") ? userMap.get("tamara") : new User("tamara", "tamara");

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            for (File authorDir : listDirs(path)) {
                for (File bookDir : listDirs(authorDir)) {
                    File metadata = new File(bookDir, METADATA_FILE);

                    Document doc = db.parse(metadata);

                    String title = getElement(doc, CalibreTag.TITLE);

                    String authorString = getElement(doc, CalibreTag.AUTHOR);
                    Author author = null;
                    if (authorMap.containsKey(authorString)) {
                        author = authorMap.get(authorString);
                    } else {
                        author = new Author(authorString);
                        authorMap.put(authorString, author);
                        calibreImport.getAuthors().add(author);
                    }

                    String description = getElement(doc, CalibreTag.DESCRIPTION);
                    if (description != null) {
                        for (String tag : DESCRIPTION_TAGS.keySet()) {
                            description = description.replace(tag, DESCRIPTION_TAGS.get(tag));
                        }
                        if (description.startsWith("\n")) {
                            description = description.substring(1);
                        }
                    }

                    Language language = null;
                    String lang = getElement(doc, CalibreTag.LANGUAGE);
                    if (lang != null) {
                        switch (lang) {
                            case "eng" -> language = Language.ENGLISH;
                            case "deu" -> language = Language.GERMAN;
                        }
                    }

                    List<DbItem> tags = new ArrayList<>();
                    tags.add(importTag);
                    calibreImport.getTags().add(importTag);

                    NodeList tagNodes = getElements(doc, CalibreTag.TAG);

                    for (int i = 0; i < tagNodes.getLength(); i++) {
                        String tagString = tagNodes.item(i).getTextContent();
                        Tag tag = null;
                        if (tagsMap.containsKey(tagString)) {
                            tag = tagsMap.get(tagString);
                        } else {
                            tag = new Tag(tagString);
                            tagsMap.put(tagString, tag);
                            calibreImport.getTags().add(tag);
                        }
                        tags.add(tag);
                    }

                    Series series = null;
                    int seriesIndex = -1;

                    NodeList metaNodes = getElements(doc, CalibreTag.META);

                    Map<String, UserBookSetting> userBookSettingMap = new HashMap<>();

                    Book book = new Book(title, author, description, language, tags, series, seriesIndex);

                    for (int i = 0; i < metaNodes.getLength(); i++) {
                        Node metaNode = metaNodes.item(i);
                        NamedNodeMap attributes = metaNode.getAttributes();
                        String name = getNamedItem(attributes, CalibreAttribute.NAME);

                        CalibreMetaName metaName = CalibreMetaName.get(name);
                        String content = getNamedItem(attributes, CalibreAttribute.CONTENT);

                        if (metaName != null) {
                            switch (metaName) {
                                case SERIES -> {
                                    if (seriesMap.containsKey(content)) {
                                        series = seriesMap.get(content);
                                    } else {
                                        series = new Series(content);
                                        seriesMap.put(content, series);
                                        calibreImport.getSeries().add(series);
                                    }
                                    book.setSeriesId(series.getId());
                                }
                                case SERIES_INDEX -> book.setSeriesIndex(Integer.parseInt(content));
                                case READ_SIMON -> {
                                    UserBookSetting userBookSetting = getUserBookSetting(userBookSettingMap, simon.getId(), book.getId());
                                    userBookSetting.setStatus(ReadingStatus.get(getCustomValue(content, "false").equals("true")));
                                }
                                case RATING_SIMON -> {
                                    UserBookSetting userBookSetting = getUserBookSetting(userBookSettingMap, simon.getId(), book.getId());
                                    userBookSetting.setRating(Integer.parseInt(getCustomValue(content, "0")) / 2);
                                }
                                case READ_TAMARA -> {
                                    UserBookSetting userBookSetting = getUserBookSetting(userBookSettingMap, tamara.getId(), book.getId());
                                    userBookSetting.setStatus(ReadingStatus.get(getCustomValue(content, "false").equals("true")));
                                }
                                case RATING_TAMARA -> {
                                    UserBookSetting userBookSetting = getUserBookSetting(userBookSettingMap, tamara.getId(), book.getId());
                                    userBookSetting.setRating(Integer.parseInt(getCustomValue(content, "0")) / 2);
                                }
                            }
                        }
                    }

                    Path file = null;
                    List<File> epubs = listFiles(bookDir).stream().filter(f -> f.getName().endsWith(".epub")).toList();
//                    book.setUserBookSettings(userBookSettingMap.values().stream().toList());
//                    book.setUserBookSettingIds(userBookSettingMap.values().stream().map(UserBookSetting::getId).toList());

                    if (epubs.size() > 0) {
                        file = epubs.get(0).toPath();
                        book.setEbook(file.toString());
                    }

                    if (!bookMap.containsKey(book.createMapKey())) {
                        calibreImport.getBooks().add(book);
                        bookMap.put(book.createMapKey(), book);

                        calibreImport.getUserBookSettings().addAll(userBookSettingMap.values().stream().toList());
                    }
                }

            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        authorService.saveAll(calibreImport.getAuthors());
        bookService.saveAll(calibreImport.getBooks());
        seriesService.saveAll(calibreImport.getSeries());
        tagService.saveAll(calibreImport.getTags());
        userBookSettingService.saveAll(calibreImport.getUserBookSettings());

        return calibreImport;
    }
}
