package com.github.jlgrock.informatix.workmanager.web.rest.dto

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *
 */
@ToString
@EqualsAndHashCode
class ResetPasswordDTO {
    String password
    String matchingPassword
}
