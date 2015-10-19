package com.github.jlgrock.informatix.workmanager.domain.tokens
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*
import javax.validation.constraints.NotNull
import java.time.LocalDateTime
/**
 * Persist AuditEvent managed by the Spring Boot actuator
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "persistent_audit_events")
class PersistentAuditEvent  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @NotNull
    @Column(nullable = false)
    String principal;

    @Column(name = "event_date")
    LocalDateTime auditEventDate;

    @Column(name = "event_type")
    String auditEventType;

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="persistent_audit_events_data", joinColumns=@JoinColumn(name="persistent_audit_event_id"))
    Map<String, String> data = new HashMap<>();

}
