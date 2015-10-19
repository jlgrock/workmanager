package com.github.jlgrock.informatix.workmanager.repository

import com.github.jlgrock.informatix.workmanager.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey)

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(LocalDateTime dateTime)

    Optional<User> findOneByResetKey(String resetKey)

    Optional<User> findOneByEmail(String email)

    Optional<User> findOneByLogin(String login)

    @Override
    void delete(User t)

}
