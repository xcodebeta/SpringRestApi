package com.bluespurs.starterkit.security;

import com.bluespurs.starterkit.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class CurrentUserImpl implements CurrentUser {
    private final SecurityContextFacade securityContext;

    @Autowired
    public CurrentUserImpl(SecurityContextFacade securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public boolean isAuthenticated() {
        return securityContext.getContext().getAuthentication().isAuthenticated();
    }

    @Override
    public boolean hasRole(String role) {
        checkNotNull(role);

        return isAuthenticated() && securityContext.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(auth -> role.equals(auth.getAuthority()));
    }

    @Override
    public User get() {
        if (!isAuthenticated()) {
            throw new SecurityException("Could not get the current user - not authenticated. Did you forget to call isAuthenticated?");
        }

        return ((UserDetailsImpl) securityContext.getContext().getAuthentication().getPrincipal()).getUser();
    }
}
