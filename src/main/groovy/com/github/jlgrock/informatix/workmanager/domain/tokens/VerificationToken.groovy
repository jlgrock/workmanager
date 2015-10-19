package com.github.jlgrock.informatix.workmanager.domain.tokens

import com.github.jlgrock.informatix.workmanager.domain.User
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull
/**
 *
 */
@Entity
@Table(name = "verification_tokens")
@ToString
@EqualsAndHashCode
class VerificationToken extends AbstractExpiry {

    @Id
    @GeneratedValue
    @NotNull
    Long id

    @NotEmpty
    String token = generateToken()

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    User user

    VerificationToken(User userIn) {
        user = userIn
    }

}
