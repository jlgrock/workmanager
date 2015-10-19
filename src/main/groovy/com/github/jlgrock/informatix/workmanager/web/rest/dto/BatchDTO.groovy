package com.github.jlgrock.informatix.workmanager.web.rest.dto

import com.github.jlgrock.informatix.workmanager.domain.Assignment
import com.github.jlgrock.informatix.workmanager.domain.Batch
import com.github.jlgrock.informatix.workmanager.domain.BatchStatus
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

/**
 *
 */
@ToString
@EqualsAndHashCode
class BatchDTO {

    Integer id

    String name

    List<AssignmentDTO> assignments

    int numberOfRecords

    AttachmentDTO original

    int numberOfUnvalidatedRecords

    AttachmentDTO postValidation

    BatchStatus status

    AttachmentDTO compiled

    boolean reviewedExists

    LocalDateTime reviewedMaxDate

    BatchDTO(Batch batch, Assignment assignmentIn) {
        this(batch, [assignmentIn])
    }

    BatchDTO(Batch batch, List<Assignment> assignmentsIn) {
        id = batch.id
        name = batch.name
        numberOfRecords = batch.numberOfRecords
        original = new AttachmentDTO(batch.original)
        numberOfUnvalidatedRecords = batch.numberOfUnvalidatedRecords
        postValidation = new AttachmentDTO(batch.postValidation)
        status = batch.status
        if (batch.compiled != null) {
            compiled = new AttachmentDTO(batch.compiled)
        }
        if (batch.reviewed != null && batch.reviewed.size() > 0) {
            reviewedExists = true;

            LocalDateTime localDateTime = null
            batch.reviewed.each {
                attachment ->
                    if (localDateTime == null || attachment.uploadDate.compareTo(localDateTime) < 0) {
                        localDateTime = attachment.uploadDate
                    }
            }
            reviewedMaxDate = localDateTime
        }

        if (assignmentsIn != null) {
            assignments = assignmentsIn.collect {
                Assignment assignment ->
                    new AssignmentDTO(assignment)
            }
        }
    }
}
