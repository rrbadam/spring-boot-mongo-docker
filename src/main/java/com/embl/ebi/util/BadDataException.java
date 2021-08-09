package com.embl.ebi.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadDataException extends EBIException {
    public BadDataException(String message) {
        super(message);
    }
}
