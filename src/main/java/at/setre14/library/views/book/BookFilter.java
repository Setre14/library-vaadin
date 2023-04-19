package at.setre14.library.views.book;

import at.setre14.library.data.author.Author;
import at.setre14.library.data.series.Series;
import at.setre14.library.data.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookFilter {
    private String title;
    private Author author;
    private Series series;
    private Tag tag;
}
