package com.github.jlgrock.informatix.workmanager.web.propertyeditors

import org.springframework.util.StringUtils

import java.beans.PropertyEditorSupport
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
/**
 * Custom PropertyEditorSupport to convert from String to
 * Date using JodaTime (http://www.joda.org/joda-time/).
 */
class LocaleDateTimeEditor extends PropertyEditorSupport {

    final DateTimeFormatter formatter

    final boolean allowEmpty

    /**
     * Create a new LocaleDateTimeEditor instance, using the given format for
     * parsing and rendering.
     * <p/>
     * The "allowEmpty" parameter states if an empty String should be allowed
     * for parsing, i.e. get interpreted as null value. Otherwise, an
     * IllegalArgumentException gets thrown.
     *
     * @param dateFormat DateFormat to use for parsing and rendering
     * @param allowEmpty if empty strings should be allowed
     */
    LocaleDateTimeEditor(String dateFormat, boolean allowEmptyIn) {
        formatter = DateTimeFormatter.ofPattern(dateFormat)
        allowEmpty = allowEmptyIn
    }

    /**
     * Format the YearMonthDay as String, using the specified format.
     *
     * @return DateTime formatted string
     */
    @Override
    String getAsText() {
        Date value = (Date) getValue()
        value != null ? LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()).format(formatter) : ""
    }

    /**
     * Parse the value from the given text, using the specified format.
     *
     * @param text the text to format
     * @throws IllegalArgumentException
     */
    @Override
    void setAsText( String text ) throws IllegalArgumentException {
        if ( allowEmpty && !StringUtils.hasText(text) ) {
            // Treat empty String as null value.
            setValue(null)
        } else {
            setValue(LocalDateTime.parse(text, formatter))
        }
    }
}
