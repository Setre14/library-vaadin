package at.setre14.library.data.userbooksettings;

import at.setre14.library.data.dbitem.DbItemService;
import org.springframework.stereotype.Service;

@Service
public class UserBookSettingService extends DbItemService<UserBookSetting> {

    public UserBookSettingService(UserBookSettingRepository repository) {
        super(repository);
    }

}
