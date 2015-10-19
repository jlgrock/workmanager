package com.github.jlgrock.informatix.workmanager.repository

import com.github.jlgrock.informatix.workmanager.config.audit.AuditEventConverter
import com.github.jlgrock.informatix.workmanager.domain.tokens.PersistentAuditEvent
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.boot.actuate.audit.AuditEventRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import javax.inject.Inject
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Wraps an implementation of Spring Boot's AuditEventRepository.
 */
@Repository
class CustomAuditEventRepository {

    @Inject
    PersistenceAuditEventRepository persistenceAuditEventRepository

    @Bean
    AuditEventRepository auditEventRepository() {
        new MyAuditEvent()
    }

    class MyAuditEvent implements AuditEventRepository {
        static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE"

        static final String ANONYMOUS_USER = "anonymousUser"

        @Inject
        AuditEventConverter auditEventConverter

        @Override
        public List<AuditEvent> find(String principal, Date after) {
            Iterable<PersistentAuditEvent> persistentAuditEvents
            if (principal == null && after == null) {
                persistentAuditEvents = persistenceAuditEventRepository.findAll()
            } else if (after == null) {
                persistentAuditEvents = persistenceAuditEventRepository.findByPrincipal(principal)
            } else {
                persistentAuditEvents =
                    persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfter(principal,
                        LocalDateTime.ofInstant(after.toInstant(), ZoneId.systemDefault()))
            }
            auditEventConverter.convertToAuditEvents(persistentAuditEvents)
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void add(AuditEvent event) {
            if(!AUTHORIZATION_FAILURE.equals(event.getType()) &&
                !ANONYMOUS_USER.equals(event.getPrincipal().toString())){

                PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent()
                persistentAuditEvent.setPrincipal(event.getPrincipal())
                persistentAuditEvent.setAuditEventType(event.getType())
                persistentAuditEvent.auditEventDate = LocalDateTime.ofInstant(event.timestamp.toInstant(), ZoneId.systemDefault())
                persistentAuditEvent.setData(auditEventConverter.convertDataToStrings(event.getData()))
                persistenceAuditEventRepository.save(persistentAuditEvent)
            }
        }
    }

}
