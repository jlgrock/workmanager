package com.github.jlgrock.informatix.workmanager.security
import com.github.jlgrock.informatix.workmanager.domain.User
import com.github.jlgrock.informatix.workmanager.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import javax.inject.Inject
import java.util.function.Function
import java.util.function.Supplier
/**
 * Authenticate a user from the database.
 */
@Component
class CustomUserDetailsService implements UserDetailsService {

    final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Inject
    UserRepository userRepository;

    @Override
    @Transactional
    UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login)
        String lowercaseLogin = login.toLowerCase()
        Optional<User> userFromDatabase =  userRepository.findOneByLogin(lowercaseLogin)
        userFromDatabase.map( new Function<User, org.springframework.security.core.userdetails.User>() {
            @Override
            org.springframework.security.core.userdetails.User apply(User user) {
                if (!user.getActivated()) {
                    throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
                }
                List<GrantedAuthority> grantedAuthorities = user.authorities.collect { new SimpleGrantedAuthority(it.name) };
                new org.springframework.security.core.userdetails.User(lowercaseLogin, user.password, grantedAuthorities);
            }
        }).orElseThrow(new Supplier<UsernameNotFoundException>() {
            @Override
            UsernameNotFoundException get() {
                new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")
            }
        });
    }
}
