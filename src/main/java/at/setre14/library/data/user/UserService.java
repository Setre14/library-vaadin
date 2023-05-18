package at.setre14.library.data.user;

import at.setre14.library.data.Role;
import at.setre14.library.data.dbitem.DbItemService;
import at.setre14.library.security.SecurityConfiguration;
import org.springframework.stereotype.Service;


@Service
public class UserService extends DbItemService<User> {
    private final SecurityConfiguration securityConfiguration;

    public UserService(UserRepository repository, SecurityConfiguration securityConfiguration) {
        super(repository);
        this.securityConfiguration = securityConfiguration;
    }

    @Override
    public void save(User user) {
        user.setHashedPassword(securityConfiguration.passwordEncoder().encode(user.getPassword()));
        super.save(user);
    }

    public boolean hasAdminUsers() {
        UserRepository userRepository = (UserRepository) repository;
        return userRepository.countByRole(Role.ADMIN) > 0;
    }
}
