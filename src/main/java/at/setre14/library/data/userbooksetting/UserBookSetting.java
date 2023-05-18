package at.setre14.library.data.userbooksetting;

import at.setre14.library.data.dbitem.DbItem;
import at.setre14.library.model.ReadingStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserBookSetting extends DbItem {
    private String userId;
    private String bookId;
    private ReadingStatus status;
    private int page = 0;
    private int rating;
    private boolean sync;

    public UserBookSetting(String userId, String bookId) {
        super(userId + ":" + bookId);
        this.userId = userId;
        this.bookId = bookId;
    }
}
