package com.github.jlgrock.informatix.workmanager.domain.tokens
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jlgrock.informatix.workmanager.domain.User
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.LocalDate
import java.time.format.DateTimeFormatter
/**
 * Persistent tokens are used by Spring Security to automatically log in users.
 *
 * @see com.github.jlgrock.informatix.workmanager.security.CustomPersistentRememberMeServices
 */
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "persistent_tokens")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class PersistentToken implements Serializable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy")

    private static final int MAX_USER_AGENT_LEN = 255

    @Id
    String series

    @JsonIgnore
    @NotNull
    @Column(name = "token_value", nullable = false)
    String tokenValue

    @JsonIgnore
    @Column(name = "token_date")
    LocalDate tokenDate

    //an IPV6 address max length is 39 characters
    @Size(min = 0, max = 39)
    @Column(name = "ip_address", length = 39)
    String ipAddress

    @Column(name = "user_agent")
    String userAgent

    @JsonIgnore
    @ManyToOne
    User user

}
