package com.github.jlgrock.informatix.workmanager.domain
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotNull
import java.time.LocalDateTime
/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
abstract class AbstractAuditingEntity {

    @CreatedBy
    @NotNull
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    String createdBy

    @CreatedDate
    @NotNull
    @Column(name = "created_date", nullable = false)
    @JsonIgnore
    LocalDateTime createdDate = LocalDateTime.now()

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    @JsonIgnore
    String lastModifiedBy

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    LocalDateTime lastModifiedDate = LocalDateTime.now()
}
