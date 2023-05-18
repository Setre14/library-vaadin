package at.setre14.library.data.userbooksetting;

import at.setre14.library.data.dbitem.DbItemService;
import org.springframework.stereotype.Service;

@Service
public class UserBookSettingService extends DbItemService<UserBookSetting> {

    public UserBookSettingService(UserBookSettingRepository repository) {
        super(repository);
    }

    public UserBookSetting find(String userId, String bookId) {
        UserBookSetting userBookSetting = ((UserBookSettingRepository) repository).findByUserIdAndBookId(userId, bookId);
        return userBookSetting != null ? userBookSetting : new UserBookSetting(userId, bookId);
    }

}
