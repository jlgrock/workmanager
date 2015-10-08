package com.github.jlgrock.informatix.workmanager.controllers

import com.github.jlgrock.informatix.workmanager.domain.batch.BatchDTO
import com.github.jlgrock.informatix.workmanager.exceptions.UploadException
import com.github.jlgrock.informatix.workmanager.exceptions.WebException
import com.github.jlgrock.informatix.workmanager.services.BatchService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
/**
 *
 */
@RestController
@RequestMapping("/batches")
class BatchResource extends AbstractSpringResource {

    static final Logger LOGGER = LoggerFactory.getLogger(AccountsResource.class)

    @Autowired
    BatchService batchService

    @RequestMapping(method = RequestMethod.POST)
    BatchDTO upload(
            @RequestParam String name,
            @RequestParam MultipartFile file) {
        LOGGER.debug("put batch: ")
        if (name == null || name.isEmpty()) {
            throw new UploadException("batch is null. Cannot continue with add")
        }
        if (file.isEmpty()) {
            throw new UploadException("Cannot process an empty file")
        }
        try {
            batchService.add(name, file.contentType, file.bytes)
        } catch(WebException we) {
            throw we
        } catch (Exception e) {
            throw new UploadException(e)
        }

    }

    @RequestMapping(method = RequestMethod.GET)
    List<BatchDTO> get(
            @RequestParam(required = false) String searchTerm) {
        LOGGER.debug("get searchTerm: ${searchTerm}")
        batchService.findAllBatches(searchTerm)
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}/complete")
    void startReview(@PathVariable int id) {
        batchService.createCompiled(id)
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}/complete")
    void deleteReview(@PathVariable int id) {
        batchService.deleteCompiled(id)
    }
}
