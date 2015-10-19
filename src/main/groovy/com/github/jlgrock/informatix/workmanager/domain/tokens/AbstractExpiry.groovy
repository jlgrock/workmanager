package com.github.jlgrock.informatix.workmanager.domain.tokens

import java.time.LocalDateTime
/**
 *
 */
abstract class AbstractExpiry {
    /**
     * the default number of hours until the expiration.
     */
    static final int DEFAULT_EXPIRATION_HOURS = 24;

    LocalDateTime expiryDate

    AbstractExpiry() {
        expiryDate = calculateExpiryDate()
    }

    protected static LocalDateTime calculateExpiryDate() {
        calculateExpiryDate( DEFAULT_EXPIRATION_HOURS )
    }

    protected static LocalDateTime calculateExpiryDate(int hours) {
        LocalDateTime localDateTime = LocalDateTime.now()
        localDateTime.plusHours(hours)
    }

    static String generateToken() {
        return UUID.randomUUID().toString()
    }
}
