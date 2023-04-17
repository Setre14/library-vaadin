package at.setre14.library.data.userbooksettings;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserBookSettingRepository extends MongoRepository<UserBookSetting, String>, DbItemRepository<UserBookSetting> {
}
