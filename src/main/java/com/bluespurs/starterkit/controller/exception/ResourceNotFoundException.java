package com.bluespurs.starterkit.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -8069608476287615632L;

    private final Class<?> resource;

    public ResourceNotFoundException(Class<?> resource) {
        super("Could not locate requested " + resource.getSimpleName() + " resource");
        this.resource = resource;
    }

    public Class<?> getResource() {
        return resource;
    }
}
