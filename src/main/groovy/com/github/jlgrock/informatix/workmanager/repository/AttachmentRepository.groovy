package com.github.jlgrock.informatix.workmanager.repository

import com.github.jlgrock.informatix.workmanager.domain.Attachment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 *
 */
@Repository
interface AttachmentRepository extends CrudRepository<Attachment, Long> {
}
