package com.github.jlgrock.informatix.workmanager.repository

import com.github.jlgrock.informatix.workmanager.domain.Batch
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
/**
 *
 */
@Repository
interface BatchRepository extends PagingAndSortingRepository<Batch, Long> {
    List<Batch> findAllByOrderByIdDesc()
    List<Batch> findAllByNameContainingIgnoreCaseOrderByIdDesc(String name)
}
