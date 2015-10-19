package com.github.jlgrock.informatix.workmanager.web.rest
import com.github.jlgrock.informatix.workmanager.domain.Attachment
import com.github.jlgrock.informatix.workmanager.repository.AttachmentRepository
import com.github.jlgrock.informatix.workmanager.web.rest.dto.AttachmentDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
/**
 *
 */
@RestController
@RequestMapping("attachments")
class AttachmentResource extends AbstractSpringResource {

    @Autowired
    AttachmentRepository attachmentRepository

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    ResponseEntity<AttachmentDTO> get(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>("id is null", HttpStatus.BAD_REQUEST)
        }
        new ResponseEntity<>(new AttachmentDTO(attachmentRepository.findOne(id)), HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}/download")
    public ResponseEntity<byte[]> getDownload(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>("id is null", HttpStatus.BAD_REQUEST)
        }
        Attachment attachment = attachmentRepository.findOne(id)

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "attachment; filename=\"${attachment.fileName}\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(attachment.data.size())
                .contentType(attachment.mediaType)
                .body(attachment.data);
    }

}
