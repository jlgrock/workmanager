package com.github.jlgrock.informatix.workmanager.web.rest.errors
/**
 * DTO for sending a parameterized error message.
 */
class ParameterizedErrorDTO implements Serializable {

    static final long serialVersionUID = 1L
    final String message
    final String[] params

    ParameterizedErrorDTO(String messageIn, String... paramsIn) {
        message = messageIn
        params = paramsIn
    }

}
