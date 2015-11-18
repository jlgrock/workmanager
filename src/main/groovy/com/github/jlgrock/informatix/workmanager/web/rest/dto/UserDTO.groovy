package com.github.jlgrock.informatix.workmanager.web.rest.dto

import com.github.jlgrock.informatix.workmanager.domain.Authority
import com.github.jlgrock.informatix.workmanager.domain.User
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.validator.constraints.Email

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
/**
 * A DTO representing a user, with his authorities.
 */
@ToString
@EqualsAndHashCode
public class UserDTO {

    static final int PASSWORD_MIN_LENGTH = 5;
    static final int PASSWORD_MAX_LENGTH = 20;

    @Pattern(regexp = "^[a-z0-9]*\$")
    @NotNull
    @Size(min = 1, max = 50)
    String login

    @NotNull
    @Size(min = 5, max = 100)
    String password

    @Size(max = 50)
    String firstName

    @Size(max = 50)
    String lastName

    @Email
    @Size(min = 5, max = 100)
    String email

    boolean activated = false

    @Size(min = 2, max = 5)
    String langKey

    Set<String> authorities

    public UserDTO(LinkedHashMap map) {
        login = map.login
        password = map.password
        firstName = map.firstName
        lastName = map.lastName
        email = map.email
        activated = map.activated
        langKey = map.langKey
        authorities = map.authorities
    }

    public UserDTO(User user) {
        this(login: user.login, password: null, firstName: user.firstName, lastName: user.lastName,
            email: user.email, activated: user.activated, langKey: user.langKey,
            authorities: user.authorities.collect { Authority auth -> auth.name } as Set<String>);
    }

    private static Set<String> convertToSetOfAuths(def auths) {
        List<String> authNameList = auths.collect { Authority auth -> auth.name }
        authNameList as Set<String>
    }
}
