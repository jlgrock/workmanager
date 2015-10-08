package com.github.jlgrock.informatix.workmanager.services
import com.github.jlgrock.informatix.workmanager.domain.assignment.Assignment
import com.github.jlgrock.informatix.workmanager.domain.assignment.AssignmentRepository
import com.github.jlgrock.informatix.workmanager.domain.assignment.AssignmentStatus
import com.github.jlgrock.informatix.workmanager.domain.assignment.HoursDTO
import com.github.jlgrock.informatix.workmanager.domain.attachment.Attachment
import com.github.jlgrock.informatix.workmanager.domain.attachment.AttachmentDTO
import com.github.jlgrock.informatix.workmanager.domain.batch.Batch
import com.github.jlgrock.informatix.workmanager.domain.batch.BatchRepository
import com.github.jlgrock.informatix.workmanager.domain.useraccount.UserAccount
import com.github.jlgrock.informatix.workmanager.domain.useraccount.UserAccountRepository
import com.github.jlgrock.informatix.workmanager.exceptions.AssignmentException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

import java.time.LocalDateTime
/**
 *
 */
@Service
class AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository

    @Autowired
    UserAccountRepository userAccountRepository

    @Autowired
    BatchRepository batchRepository

    @Autowired
    BatchService batchService

    Assignment get(int id) {
        assignmentRepository.findOne(id)
    }

    void delete(int id) {
        Assignment assignment = assignmentRepository.findOne(id)
        try {
            assignmentRepository.delete(id)
        } catch (EmptyResultDataAccessException e) {
            throw new AssignmentException(e)
        }
        batchService.updateBatchStatus(assignment.batch.id)
        assignment
    }

    Assignment addNewAssignment(int userId, int batchId, String name,
                                String contentType, byte[] data) {
        Assignment assignment = addAssignment(userId, batchId, name, contentType, data, AssignmentStatus.UNSTARTED)
        batchService.updateBatchStatus(assignment.batch.id)
        assignment
    }
    Assignment addAssignment(int userId, int batchId, String name,
                                String contentType, byte[] data, AssignmentStatus status) {

        UserAccount userAccount = userAccountRepository.findOne(userId)
        Batch batch = batchRepository.findOne(batchId)

        Attachment original = new Attachment(
                fileName: name,
                uploadDate: LocalDateTime.now(),
                numberOfBytes: data.size(),
                mediaType: MediaType.parseMediaType(contentType),
                data: data);

        // TODO process file to determine number of records
        Assignment assignment = new Assignment([
                batch: batch,
                userAccount: userAccount,
                numberOfRecords: 34, //TODO fix after processing
                original: original,
                isAutoGenerated: false,
                status: status
        ])
        assignmentRepository.save(assignment)
        //TODO send email

    }

    Assignment completeAssignment(int assignmentId, String name, String contentType, byte[] data) {
        Assignment assignment = assignmentRepository.findOne(assignmentId)

        Attachment completed = new Attachment([
                fileName: name,
                uploadDate: LocalDateTime.now(),
                numberOfBytes: data.size(),
                mediaType: MediaType.parseMediaType(contentType),
                data: data
        ])

        assignment.completed.add(completed)

        batchService.updateBatchStatus(assignment.batch.id)
        assignment.status = determineAssignmentStatus(assignment)
        assignment = assignmentRepository.save(assignment)
        assignment
    }

    AssignmentStatus determineAssignmentStatus(Assignment assignment) {
        if (assignment.billableHours != null) {
            return AssignmentStatus.COMPLETED
        }

        if (assignment.completed != null && assignment.completed.size() > 0) {
            // TODO adjust status based on criteria.  Currently just picking NEEDS_HOURS all the time
            return AssignmentStatus.NEEDS_HOURS
        }

        return AssignmentStatus.UNSTARTED
    }

    Assignment getAssignmentForHours(int id) {
        Assignment assignment = assignmentRepository.findOne(id)

        if (assignment.completed == null) {
            throw new AssignmentException("Cannot set hours until assignment has been completed")
        }
        assignment
    }
    Assignment setHours(int assignmentId, HoursDTO hoursDTO) {
        Assignment assignment = getAssignmentForHours(assignmentId)
        assignment.billableHours = hoursDTO.hours

        assignment.status = determineAssignmentStatus(assignment)
        assignment = assignmentRepository.save(assignment)
        batchService.updateBatchStatus(assignment.batch.id)
        assignment
    }

    Assignment clearHours(int assignmentId) {
        Assignment assignment = getAssignmentForHours(assignmentId)
        assignment.billableHours = null

        batchService.updateBatchStatus(assignment.batch.id)
        assignment.status = determineAssignmentStatus(assignment)
        assignment = assignmentRepository.save(assignment)
        assignment
    }

    Collection<AttachmentDTO> getCompletedHistory(int assignmentId) {
        Assignment assignment = assignmentRepository.findOne(assignmentId)
        assignment.completed.collect {
            Attachment attachment ->
            new AttachmentDTO(attachment)
        }
    }
}
