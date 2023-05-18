package at.setre14.library.security;

import at.setre14.library.data.user.User;
import at.setre14.library.data.user.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    public User get() {
        Optional<User> optionalUser = authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByName(userDetails.getUsername()));
        return optionalUser.orElse(null);
    }

    public User waitAndGet() {
        Optional<UserDetails> optionalUserDetails = Optional.empty();

        while (optionalUserDetails.isEmpty()) {
            optionalUserDetails = authenticationContext.getAuthenticatedUser(UserDetails.class);
        }


        return userRepository.findByName(optionalUserDetails.get().getUsername());
    }

    public void logout() {
        authenticationContext.logout();
    }

}
