package com.github.jlgrock.informatix.workmanager.web.rest.dto

import com.github.jlgrock.informatix.workmanager.domain.User
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime


/**
 * A DTO extending the UserDTO, which is meant to be used in the user management UI.
 */
@ToString
@EqualsAndHashCode
class ManagedUserDTO extends UserDTO {

    Long id;

    LocalDateTime createdDate;

    String lastModifiedBy;

    LocalDateTime lastModifiedDate;

    ManagedUserDTO(User user) {
        super(user);
        id = user.id
        createdDate = user.createdDate
        lastModifiedBy = user.lastModifiedBy
        lastModifiedDate = user.lastModifiedDate
    }

}
