package at.setre14.library.data.user;

import at.setre14.library.data.Role;
import at.setre14.library.data.dbitem.DbItemRepository;

public interface UserRepository extends DbItemRepository<User> {
    int countByRole(Role role);

}
