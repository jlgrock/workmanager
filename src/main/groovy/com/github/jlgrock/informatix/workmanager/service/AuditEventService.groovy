package com.github.jlgrock.informatix.workmanager.service
import com.github.jlgrock.informatix.workmanager.config.audit.AuditEventConverter
import com.github.jlgrock.informatix.workmanager.domain.tokens.PersistentAuditEvent
import com.github.jlgrock.informatix.workmanager.repository.PersistenceAuditEventRepository
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.inject.Inject
import java.time.LocalDateTime

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
@Transactional
class AuditEventService {

    PersistenceAuditEventRepository persistenceAuditEventRepository
    AuditEventConverter auditEventConverter

    @Inject
    AuditEventService(
            PersistenceAuditEventRepository persistenceAuditEventRepositoryIn,
            AuditEventConverter auditEventConverterIn) {
        persistenceAuditEventRepository = persistenceAuditEventRepositoryIn
        auditEventConverter = auditEventConverterIn
    }

    public List<AuditEvent> findAll() {
        auditEventConverter.convertToAuditEvents(persistenceAuditEventRepository.findAll())
    }

    public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
        List<PersistentAuditEvent> persistentAuditEvents =
                persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate)

        auditEventConverter.convertToAuditEvents(persistentAuditEvents)
    }

    public Optional<AuditEvent> find(Long id) {
        PersistentAuditEvent persistentAuditEvent = persistenceAuditEventRepository.findOne(id)
        if (persistentAuditEvent == null) {
            return Optional.empty()
        }
        Optional.of(auditEventConverter.convertToAuditEvent(persistentAuditEvent))
    }

}
