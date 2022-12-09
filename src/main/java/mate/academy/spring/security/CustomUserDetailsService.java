package mate.academy.spring.security;

import mate.academy.spring.model.User;
import java.util.Optional;
import org.springframework.security.core.userdetails.User.*;
import static org.springframework.security.core.userdetails.User.withUsername;
import mate.academy.spring.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findByEmail(email);
        UserBuilder userBuilder;
        if (userOptional.isPresent()) {
            userBuilder = withUsername(email);
            userBuilder.password(userOptional.get().getPassword());
            userBuilder.roles(userOptional.get().getRoles()
                    .stream()
                    .map(x -> x.getRoleName().name())
                    .toArray(String[]::new));
            return userBuilder.build();
        }
        throw new UsernameNotFoundException("User not found.");
    }
}
