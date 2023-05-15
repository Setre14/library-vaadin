package at.setre14.library.data.user;

import at.setre14.library.data.dbitem.DbItemService;
import org.springframework.stereotype.Service;


@Service
public class UserService extends DbItemService<User> {
    public UserService(UserRepository repository) {
        super(repository);
    }
}
