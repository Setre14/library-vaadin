package at.setre14.library.data.userbooksettings;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserBookSettingRepository extends MongoRepository<UserBookSetting, String> {
}