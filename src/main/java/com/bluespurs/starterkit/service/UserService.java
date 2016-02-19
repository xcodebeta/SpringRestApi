package com.bluespurs.starterkit.service;

import com.bluespurs.starterkit.domain.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    User create(String email, String password);
    void update(User user);
    void update(User user, String password);
    Optional<User> getUser(long userId);
    Optional<User> getUserByEmail(String email);
    Page<User> getAllUsers(int currentPage, int itemsPerPage);
}
