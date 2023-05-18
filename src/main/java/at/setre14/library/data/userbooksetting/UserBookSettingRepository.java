package at.setre14.library.data.userbooksetting;


import at.setre14.library.data.dbitem.DbItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookSettingRepository extends DbItemRepository<UserBookSetting> {
    UserBookSetting findByUserIdAndBookId(String userId, String bookId);
}
