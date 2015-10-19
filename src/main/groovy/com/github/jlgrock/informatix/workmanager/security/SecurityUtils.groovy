package com.github.jlgrock.informatix.workmanager.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
/**
 * Utility class for Spring Security.
 */
final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     */
    static String getCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.context
        Authentication authentication = securityContext.authentication
        UserDetails springSecurityUser = null;
        String userName = null;
        if(authentication != null) {
            if (authentication.principal instanceof UserDetails) {
                springSecurityUser = (UserDetails) authentication.principal;
                userName = springSecurityUser.username;
            } else if (authentication.principal instanceof String) {
                userName = (String) authentication.principal;
            }
        }
        return userName;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.context
        Collection<? extends GrantedAuthority> authorities = securityContext.authentication.authorities
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                if (authority.authority.equals(AuthoritiesConstants.ANONYMOUS)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * If the current user has a specific authority (security role).
     *
     * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
     */
    static boolean isUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.context
        Authentication authentication = securityContext.authentication
        if(authentication != null) {
            if (authentication.principal instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.principal
                return springSecurityUser.authorities.contains(new SimpleGrantedAuthority(authority))
            }
        }
        return false;
    }
}
