package com.example.hs2actors.controller.exceptions.handlers;

import com.example.hs2actors.controller.exceptions.ControllerException;
import com.example.hs2actors.controller.exceptions.fallback.ServiceUnavailableException;
import com.example.hs2actors.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CircuitBreakingExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected ErrorDTO handleServiceUnavailability(ControllerException ex) {
        return new ErrorDTO(
                ex.getTimestamp(),
                ex.getMessage(),
                ex.getError()
        );
    }

}
