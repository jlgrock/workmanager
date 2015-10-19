package com.github.jlgrock.informatix.workmanager.config.audit

import com.github.jlgrock.informatix.workmanager.domain.tokens.PersistentAuditEvent
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component

import java.time.ZoneId
import java.util.*

@Component
class AuditEventConverter {

    /**
     * Convert a list of PersistentAuditEvent to a list of AuditEvent
     *
     * @param persistentAuditEvents the list to convert
     * @return the converted list.
     */
    List<AuditEvent> convertToAuditEvents(Iterable<PersistentAuditEvent> persistentAuditEvents) {
        if (persistentAuditEvents == null) {
            return Collections.emptyList()
        }

        List<AuditEvent> auditEvents = new ArrayList<>()

        for (PersistentAuditEvent persistentAuditEvent : persistentAuditEvents) {
            auditEvents.add(convertToAuditEvent(persistentAuditEvent))
        }

        auditEvents
    }

    /**
     * Convert a PersistentAuditEvent to an AuditEvent
     *
     * @param persistentAuditEvent the event to convert
     * @return the converted list.
     */
    AuditEvent convertToAuditEvent(PersistentAuditEvent persistentAuditEvent) {
        new AuditEvent(
            Date.from(persistentAuditEvent.auditEventDate.atZone(ZoneId.systemDefault()).toInstant()),
            persistentAuditEvent.principal,
            persistentAuditEvent.auditEventType,
            convertDataToObjects(persistentAuditEvent.data))
    }

    /**
     * Internal conversion. This is needed to support the current SpringBoot actuator AuditEventRepository interface
     *
     * @param data the data to convert
     * @return a map of String, Object
     */
    Map<String, Object> convertDataToObjects(Map<String, String> data) {
        Map<String, Object> results = new HashMap<>()

        if (data != null) {
            for (String key : data.keySet()) {
                results.put(key, data.get(key))
            }
        }

        results
    }

    /**
     * Internal conversion. This method will allow to save additional data.
     * By default, it will save the object as string
     *
     * @param data the data to convert
     * @return a map of String, String
     */
    Map<String, String> convertDataToStrings(Map<String, Object> data) {
        Map<String, String> results = new HashMap<>()

        if (data != null) {
            for (String key : data.keySet()) {
                Object object = data.get(key)

                // Extract the data that will be saved.
                if (object instanceof WebAuthenticationDetails) {
                    WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails) object
                    results.put("remoteAddress", authenticationDetails.remoteAddress)
                    results.put("sessionId", authenticationDetails.sessionId)
                } else if (object != null) {
                    results.put(key, object.toString())
                } else {
                    results.put(key, "null")
                }
            }
        }

        results
    }
}
