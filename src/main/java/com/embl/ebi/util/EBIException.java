package com.embl.ebi.util;

import lombok.Data;

@Data
public class EBIException extends RuntimeException {
    private String errorCode;
    private String resource;
    public EBIException(String message) {
        super(message);
    }
}
