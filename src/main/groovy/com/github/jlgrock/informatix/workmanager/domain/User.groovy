package com.github.jlgrock.informatix.workmanager.domain
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jlgrock.informatix.workmanager.domain.tokens.PersistentToken
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.validator.constraints.Email

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.time.LocalDateTime
/**
 * A user.
 */
@Entity
@Table(name = "user_accounts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class User extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id

    @NotNull
    @Pattern(regexp = '^[a-z0-9]*$|(anonymousUser)')
    @Size(min = 1, max = 50)
    @Column(name="login", length = 50, unique = true, nullable = false)
    String login

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name="password", length = 60)
    String password

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    String firstName

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    String lastName

    @Email
    @Size(max = 100)
    @Column(name="email", length = 100, unique = true)
    String email

    @Column(name="activated", nullable = false)
    boolean activated = false

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    String langKey

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    String activationKey

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    String resetKey

    @Column(name = "reset_date", nullable = true)
    LocalDateTime resetDate = null

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name")
    )

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    Set<Authority> authorities = new HashSet<>()

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    Set<PersistentToken> persistentTokens = new HashSet<>()
}
