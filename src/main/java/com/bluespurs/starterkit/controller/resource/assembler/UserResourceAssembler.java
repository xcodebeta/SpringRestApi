package com.bluespurs.starterkit.controller.resource.assembler;

import com.bluespurs.starterkit.controller.UserController;
import com.bluespurs.starterkit.controller.resource.UserResource;
import com.bluespurs.starterkit.domain.User;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements Assembler<User, UserResource> {
    @Override
    public UserResource assemble(User user) {
        UserResource resource = new UserResource();

        resource.setEmail(user.getEmail());
        resource.setUserId(user.getId());

        // HATEOAS links
        resource.add(linkTo(methodOn(UserController.class).getUserProfile(user.getId())).withSelfRel());

        return resource;
    }
}
