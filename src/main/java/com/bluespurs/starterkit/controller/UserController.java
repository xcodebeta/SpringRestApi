package com.bluespurs.starterkit.controller;

import com.bluespurs.starterkit.controller.exception.DuplicateResourceException;
import com.bluespurs.starterkit.controller.exception.ResourceNotFoundException;
import com.bluespurs.starterkit.controller.request.UserInfoRequest;
import com.bluespurs.starterkit.controller.resource.PagedCollection;
import com.bluespurs.starterkit.controller.resource.UserResource;
import com.bluespurs.starterkit.controller.resource.assembler.UserResourceAssembler;
import com.bluespurs.starterkit.domain.Role;
import com.bluespurs.starterkit.domain.User;
import com.bluespurs.starterkit.security.CurrentUser;
import com.bluespurs.starterkit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final int MAX_USERS_PER_PAGE = 100;

    private final UserService userService;
    private final CurrentUser currentUser;
    private final UserResourceAssembler userAssembler;

    @Autowired
    public UserController(UserService userService, CurrentUser currentUser, UserResourceAssembler userAssembler) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.userAssembler = userAssembler;
    }

    @RequestMapping
    public PagedCollection<UserResource> getUserDirectory(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int perPage
    ) {
        if (perPage > MAX_USERS_PER_PAGE) {
            LOGGER.warn("Requested {} users per page but max is {}, overriding input", perPage, MAX_USERS_PER_PAGE);
            perPage = MAX_USERS_PER_PAGE;
        }

        Page<User> users = userService.getAllUsers(page, perPage);
        return userAssembler.assemble(users);
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserResource registerNewUser(@Valid @RequestBody UserInfoRequest request) {
        try {
            User user = userService.create(request.getEmail(), request.getPassword());
            return userAssembler.assemble(user);
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Attempted to register an account with an email in use", e);
            throw new DuplicateResourceException("email");
        }
    }

    @Secured({Role.USER, Role.ADMIN})
    @RequestMapping(method = RequestMethod.PUT)
    public UserResource updateUserInfo(@Valid @RequestBody UserInfoRequest request) {
        User user = currentUser.get();
        LOGGER.info("Updating user ID {}", user.getId());

        user.setEmail(request.getEmail());

        try {
            userService.update(user, request.getPassword());
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Attempted to update user account to use an email in use", e);
            throw new DuplicateResourceException("email");
        }

        return userAssembler.assemble(user);
    }

    @Secured({Role.USER, Role.ADMIN})
    @RequestMapping("/current")
    public UserResource getCurrentUser() {
        return userAssembler.assemble(currentUser.get());
    }

    @RequestMapping("/{id}")
    public UserResource getUserProfile(@PathVariable long id) {
        User user = userService.getUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class));

        LOGGER.info("Loaded user with ID {}", user.getId());
        return userAssembler.assemble(user);
    }
}