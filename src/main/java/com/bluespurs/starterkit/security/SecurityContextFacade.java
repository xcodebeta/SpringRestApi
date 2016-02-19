package com.bluespurs.starterkit.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * The static access to the security context makes unit testing difficult.
 * This class acts as a facade to the context holder to make unit testing (specifically mocking) easier.
 * As long as SecurityContextHolder#getContext() is thread safe, as is this.
 */
@Component
public class SecurityContextFacade {
    public SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }
}
