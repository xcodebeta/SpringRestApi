package com.bluespurs.starterkit.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    private static final long serialVersionUID = -671445966173499243L;

    private final String duplicateField;

    public DuplicateResourceException(String field) {
        super("Duplicate record for '" + field + "'");
        this.duplicateField = field;
    }

    public String getDuplicateField() {
        return duplicateField;
    }
}
