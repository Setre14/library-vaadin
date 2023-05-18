package at.setre14.library.data.book;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.tag.Tag;
import at.setre14.library.data.userbooksettings.UserBookSetting;
import at.setre14.library.model.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Transient;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Document
@ToString
public class Book extends DbItem {
    private String authorId;
    @Transient
    private Author author;
    private List<String> tagIds;
    @Transient
    private Set<Tag> tags = new HashSet<>();
    private String seriesId;
    @Transient
    private Series series;
    private int seriesIndex;
    private String description;
    private Language language;

    private String ebook;
    private boolean physical;
    private List<String> userBookSettingIds;
    @Transient
    private List<UserBookSetting> userBookSettings;

    public Book() {
        super();
        this.authorId = UUID.randomUUID().toString();
        this.tagIds = new ArrayList<>();
        this.seriesId = UUID.randomUUID().toString();
        this.seriesIndex = 1;
        this.description = "description";
        this.language = Language.GERMAN;
        this.userBookSettingIds = new ArrayList<>();
    }

    public Book(String name) {
        super(name);
        this.authorId = UUID.randomUUID().toString();
        this.tagIds = new ArrayList<>();
        this.seriesId = UUID.randomUUID().toString();
        this.seriesIndex = 1;
        this.description = "description";
        this.language = Language.GERMAN;
        this.userBookSettingIds = new ArrayList<>();
    }

    public Book(String name, DbItem authorId, String description, Language language, List<DbItem> tagIds, DbItem seriesId, int seriesIndex) {
        this();
        this.name = name;
        this.authorId = authorId.getId();
        this.description = description;
        this.language = language;
        this.tagIds = tagIds.stream().map(DbItem::getId).collect(Collectors.toList());
        this.seriesId = seriesId != null ? seriesId.getId() : null;
        this.seriesIndex = seriesIndex;
    }

    public String createMapKey() {
        return String.format("%s:%s", name, authorId);
    }

    public List<Tag> getSortedTags() {
        if (this.tags != null) {
            return tags.stream().sorted(Comparator.comparing(a -> a.getName().toLowerCase())).toList();
        }
        return new ArrayList<>();
    }
}
