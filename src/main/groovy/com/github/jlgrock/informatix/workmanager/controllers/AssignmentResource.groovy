package com.github.jlgrock.informatix.workmanager.controllers

import com.github.jlgrock.informatix.workmanager.domain.assignment.AssignmentDTO
import com.github.jlgrock.informatix.workmanager.domain.assignment.HoursDTO
import com.github.jlgrock.informatix.workmanager.domain.attachment.AttachmentDTO
import com.github.jlgrock.informatix.workmanager.exceptions.UploadException
import com.github.jlgrock.informatix.workmanager.exceptions.UserException
import com.github.jlgrock.informatix.workmanager.exceptions.WebException
import com.github.jlgrock.informatix.workmanager.services.AssignmentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
/**
 *
 */
@RestController
@RequestMapping("/assignments")
class AssignmentResource extends AbstractSpringResource {

    static final Logger LOGGER = LoggerFactory.getLogger(AssignmentResource.class)

    @Autowired
    AssignmentService assignmentService

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    AssignmentDTO get(@PathVariable int id) {
        if (id == null) {
            throw new UserException("id is null. Cannot continue with edit")
        }
        new AssignmentDTO(assignmentService.get(id))
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}")
    void delete(@PathVariable int id) {
        if (id == null) {
            throw new UserException("id is null. Cannot continue with delete")
        }
        new AssignmentDTO(assignmentService.delete(id))
    }

    @RequestMapping(method = RequestMethod.POST, value="")
    AssignmentDTO createAssignment(
            @RequestParam int userId,
            @RequestParam int batchId,
            @RequestParam String name,
            @RequestParam MultipartFile file) {
        LOGGER.debug("new assignment: ${userId}, ")
        if (name == null || name.isEmpty()) {
            throw new UploadException("batch is null. Cannot continue with add")
        }
        if (file.isEmpty()) {
            throw new UploadException("Cannot process an empty file")
        }
        try {
            new AssignmentDTO(assignmentService.addNewAssignment(userId, batchId, name, file.contentType, file.bytes))
        } catch(WebException we) {
            throw we
        } catch (Exception e) {
            throw new UploadException(e)
        }
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}")
    AssignmentDTO completeAssignment(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam MultipartFile file) {
        LOGGER.debug("completing assignment for ${id}")
        if (name == null || name.isEmpty()) {
            throw new UploadException("batch is null. Cannot continue with add")
        }
        if (file.isEmpty()) {
            throw new UploadException("Cannot process an empty file")
        }
        try {
            new AssignmentDTO(assignmentService.completeAssignment(id, name, file.contentType, file.bytes))
        } catch(WebException we) {
            throw we
        } catch (Exception e) {
            throw new UploadException(e)
        }
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}/hours")
    void updateHours(@PathVariable int id,
                           @RequestBody HoursDTO hoursDTO) {
        LOGGER.debug("Updating hours for ${id} with ${hoursDTO}")
        assignmentService.setHours(id, hoursDTO)
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}/hours")
    void clearHours(@PathVariable int id) {
        LOGGER.debug("Removing hours for ${id}")
        assignmentService.clearHours(id)
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/completed")
    List<AttachmentDTO> getCompletedHistory(@PathVariable int id) {
        LOGGER.debug("get completed history for ${id}")
        assignmentService.getCompletedHistory(id)
    };

}
