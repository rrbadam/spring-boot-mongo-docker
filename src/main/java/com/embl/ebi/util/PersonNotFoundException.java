package com.embl.ebi.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends EBIException {

    public PersonNotFoundException(String message) {
        super(message);
    }
}