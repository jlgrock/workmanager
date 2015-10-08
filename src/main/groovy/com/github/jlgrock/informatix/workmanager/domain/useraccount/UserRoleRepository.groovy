package com.github.jlgrock.informatix.workmanager.domain.useraccount

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
/**
 *
 */
@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {

    @Query("FROM UserRole r WHERE r.userRolePK.userAccount = :userAccount")
    List<UserRole> findUserRolesByUserAccount(@Param("userAccount") UserAccount userAccount)

}
