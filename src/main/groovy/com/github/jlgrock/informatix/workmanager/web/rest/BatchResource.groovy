package com.github.jlgrock.informatix.workmanager.web.rest
import com.github.jlgrock.informatix.workmanager.service.BatchService
import com.github.jlgrock.informatix.workmanager.web.rest.dto.AttachmentDTO
import com.github.jlgrock.informatix.workmanager.web.rest.dto.BatchDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
/**
 *
 */
@RestController
@RequestMapping("/batches")
class BatchResource extends AbstractSpringResource {

    @Autowired
    BatchService batchService

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<BatchDTO> upload(
            @RequestParam String name,
            @RequestParam MultipartFile file) {
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<>("batch is null. Cannot continue with add", HttpStatus.BAD_REQUEST)
        }
        if (file.isEmpty()) {
            return new ResponseEntity<>("Cannot process an empty file", HttpStatus.BAD_REQUEST)
        }

        new ResponseEntity<>(batchService.add(name, file.contentType, file.bytes), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    List<BatchDTO> get(
            @RequestParam(required = false) String searchTerm) {
        batchService.findAllBatches(searchTerm)
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}/complete")
    void startReview(@PathVariable Long id) {
        batchService.createCompiled(id)
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}/complete")
    void deleteReview(@PathVariable Long id) {
        batchService.deleteCompiled(id)
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}/completed")
    ResponseEntity<List<AttachmentDTO>> getCompletedHistory(@PathVariable Long id) {
        return new ResponseEntity<>(batchService.getCompletedHistory(id), HttpStatus.OK);
    }
}
