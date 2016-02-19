package com.bluespurs.starterkit.security;

import com.bluespurs.starterkit.domain.User;

public interface CurrentUser {
    boolean isAuthenticated();
    boolean hasRole(String role);
    User get();
}
