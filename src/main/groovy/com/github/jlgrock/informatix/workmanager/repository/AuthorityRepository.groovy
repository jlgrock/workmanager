package com.github.jlgrock.informatix.workmanager.repository

import com.github.jlgrock.informatix.workmanager.domain.Authority
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Authority entity.
 */
@Repository
interface AuthorityRepository extends JpaRepository<Authority, String> {
}
