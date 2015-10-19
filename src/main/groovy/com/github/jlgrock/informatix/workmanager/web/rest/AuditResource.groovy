package com.github.jlgrock.informatix.workmanager.web.rest
import com.github.jlgrock.informatix.workmanager.service.AuditEventService
import com.github.jlgrock.informatix.workmanager.web.propertyeditors.LocaleDateTimeEditor
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*

import javax.inject.Inject
import java.time.LocalDateTime

/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping(value = "/api/audits", produces = MediaType.APPLICATION_JSON_VALUE)
class AuditResource extends AbstractSpringResource {

    AuditEventService auditEventService

    @Inject
    AuditResource(AuditEventService auditEventService) {
        this.auditEventService = auditEventService
    }

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new LocaleDateTimeEditor("yyyy-MM-dd", false))
    }

    @RequestMapping(method = RequestMethod.GET)
    List<AuditEvent> getAll() {
        return auditEventService.findAll()
    }

    @RequestMapping(method = RequestMethod.GET, params = ["fromDate", "toDate"])
    List<AuditEvent> getByDates(@RequestParam(value = "fromDate") LocalDateTime fromDate,
                                       @RequestParam(value = "toDate") LocalDateTime toDate) {
        return auditEventService.findByDates(fromDate, toDate)
    }

    @RequestMapping(value = "/{id:.+}",
            method = RequestMethod.GET)
    ResponseEntity<AuditEvent> get(@PathVariable Long id) {
        Optional<AuditEvent> auditEventOptional = auditEventService.find(id)
        if (auditEventOptional.present) {
            return new ResponseEntity<>(auditEventOptional.get(), HttpStatus.OK)
        }
        new ResponseEntity<>(HttpStatus.NOT_FOUND)
    }
}
