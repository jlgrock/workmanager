package com.github.jlgrock.informatix.workmanager.repository
import com.github.jlgrock.informatix.workmanager.domain.tokens.PersistentAuditEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime

/**
 * Spring Data JPA repository for the PersistentAuditEvent entity.
 */
@Repository
interface PersistenceAuditEventRepository extends JpaRepository<PersistentAuditEvent, Long> {

    List<PersistentAuditEvent> findByPrincipal(String principal)

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, LocalDateTime after)

    List<PersistentAuditEvent> findAllByAuditEventDateBetween(LocalDateTime fromDate, LocalDateTime toDate)
}
