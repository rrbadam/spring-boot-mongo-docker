package com.embl.ebi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionalHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = PersonNotFoundException.class)
    public @ResponseBody
    Map<String, Object> exception(PersonNotFoundException exception) {
        return formatExceptionDetails(exception);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadDataException.class)
    public @ResponseBody
    Map<String, Object> exception(BadDataException exception) {
        return formatExceptionDetails(exception);
    }




    private Map<String, Object> formatExceptionDetails(EBIException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("errorcode", exception.getErrorCode());
        body.put("resource", exception.getResource());
        log.error(body.toString());
        return body;
    }

}
