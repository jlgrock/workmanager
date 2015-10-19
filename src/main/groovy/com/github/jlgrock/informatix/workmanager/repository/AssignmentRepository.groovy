package com.github.jlgrock.informatix.workmanager.repository

import com.github.jlgrock.informatix.workmanager.domain.Assignment
import com.github.jlgrock.informatix.workmanager.domain.Batch
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 *
 */
@Repository
interface AssignmentRepository extends CrudRepository<Assignment, Long> {
    List<Assignment> findAllByBatchOrderByIdAsc(Batch batch)
}
