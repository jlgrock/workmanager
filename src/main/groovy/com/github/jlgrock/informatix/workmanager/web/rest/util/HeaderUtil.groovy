package com.github.jlgrock.informatix.workmanager.web.rest.util

import org.springframework.http.HttpHeaders

/**
 * Utility class for http header creation.
 *
 */
class HeaderUtil {

    static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders()
        headers.add("X-workmanagerApp-alert", message)
        headers.add("X-workmanagerApp-params", param)
        return headers
    }

    static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        createAlert("workmanagerApp." + entityName + ".created", param)
    }

    static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        createAlert("workmanagerApp." + entityName + ".updated", param)
    }

    static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        createAlert("workmanagerApp." + entityName + ".deleted", param)
    }

}
