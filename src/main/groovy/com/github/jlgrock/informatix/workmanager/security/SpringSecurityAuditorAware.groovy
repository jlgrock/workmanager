package com.github.jlgrock.informatix.workmanager.security;

import com.github.jlgrock.informatix.workmanager.config.Constants
import com.github.jlgrock.informatix.workmanager.security.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentLogin();
        return (userName != null ? userName : Constants.SYSTEM_ACCOUNT)
    }
}
