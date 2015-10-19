package com.github.jlgrock.informatix.workmanager.web.rest

import com.github.jlgrock.informatix.workmanager.service.AssignmentService
import com.github.jlgrock.informatix.workmanager.web.rest.dto.AssignmentDTO
import com.github.jlgrock.informatix.workmanager.web.rest.dto.AttachmentDTO
import com.github.jlgrock.informatix.workmanager.web.rest.dto.HoursDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Collection<AssignmentDTO>> getAll() {
        new ResponseEntity<>(assignmentService.getAll().collect({new AssignmentDTO(it)}), HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    ResponseEntity<AssignmentDTO> get(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>("id is null", HttpStatus.BAD_REQUEST)
        }
        new ResponseEntity<>(assignmentService.get(id), HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}")
    ResponseEntity<AssignmentDTO> delete(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>("id is null", HttpStatus.BAD_REQUEST)
        }
        new ResponseEntity<>(assignmentService.delete(id), HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.POST, value="")
    ResponseEntity<AssignmentDTO> createAssignment(
            @RequestParam int userId,
            @RequestParam int batchId,
            @RequestParam String name,
            @RequestParam MultipartFile file) {
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<>("batch is null", HttpStatus.BAD_REQUEST)
        }
        if (file.isEmpty()) {
            return new ResponseEntity<>("batch is empty file", HttpStatus.BAD_REQUEST)
        }
        new ResponseEntity<>(
            new AssignmentDTO(assignmentService.addNewAssignment(userId, batchId, name, file.contentType, file.bytes)),
            HttpStatus.OK
        )
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}")
    ResponseEntity<AssignmentDTO> completeAssignment(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam MultipartFile file) {
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<>("assignment is null", HttpStatus.BAD_REQUEST)
        }
        if (file.isEmpty()) {
            return new ResponseEntity<>("assignment is empty file", HttpStatus.BAD_REQUEST)
        }
        new ResponseEntity<> (
            assignmentService.completeAssignment(id, name, file.contentType, file.bytes),
            HttpStatus.OK
        )
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}/hours")
    void updateHours(@PathVariable Long id,
                           @RequestBody HoursDTO hoursDTO) {
        assignmentService.setHours(id, hoursDTO)
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}/hours")
    void clearHours(@PathVariable Long id) {
        assignmentService.clearHours(id)
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/completed")
    List<AttachmentDTO> getCompletedHistory(@PathVariable Long id) {
        assignmentService.getCompletedHistory(id)
    };

}
