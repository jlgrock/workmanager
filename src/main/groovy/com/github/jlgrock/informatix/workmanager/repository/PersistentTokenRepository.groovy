package com.github.jlgrock.informatix.workmanager.repository

import com.github.jlgrock.informatix.workmanager.domain.User
import com.github.jlgrock.informatix.workmanager.domain.tokens.PersistentToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDate
/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
@Repository
interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(User user)

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate)

}
