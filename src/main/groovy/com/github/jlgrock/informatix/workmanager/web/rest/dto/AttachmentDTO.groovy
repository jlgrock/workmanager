package com.github.jlgrock.informatix.workmanager.web.rest.dto

import com.github.jlgrock.informatix.workmanager.domain.Attachment
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.NotNull
import java.time.LocalDateTime
/**
 *
 */
@ToString
@EqualsAndHashCode
class AttachmentDTO {

    @NotNull
    Integer id

    @NotEmpty
    String fileName

    @NotNull
    LocalDateTime uploadDate

    @NotNull
    int numberOfBytes

    AttachmentDTO(Attachment attachment) {
        id = attachment.id
        fileName = attachment.fileName
        uploadDate = attachment.uploadDate
        numberOfBytes = attachment.numberOfBytes
    }
}
