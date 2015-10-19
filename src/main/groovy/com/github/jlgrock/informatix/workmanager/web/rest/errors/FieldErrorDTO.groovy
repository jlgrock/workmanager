package com.github.jlgrock.informatix.workmanager.web.rest.errors

class FieldErrorDTO implements Serializable {

    static final long serialVersionUID = 1L

    final String objectName

    final String field

    final String message

    FieldErrorDTO(String dto, String fieldIn, String messageIn) {
        objectName = dto
        field = fieldIn
        message = messageIn
    }

}
