package com.example.hs2actors.controller.exceptions.handlers;

import com.example.hs2actors.controller.exceptions.already_applied.AlreadyAppliedException;
import com.example.hs2actors.controller.exceptions.not_found.NotFoundException;
import com.example.hs2actors.model.dto.ErrorDTO;
import com.example.hs2actors.model.dto.ViolationDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorDTO handleNotFound(NotFoundException ex) {
        return new ErrorDTO(ex.getTimestamp(), ex.getMessage(), ex.getError());
    }


    @ExceptionHandler(AlreadyAppliedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorDTO handleAlreadyApplied(AlreadyAppliedException ex) {
        return new ErrorDTO(ex.getTimestamp(), ex.getMessage(), ex.getError());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorDTO handleNotValidBody(ConstraintViolationException ex) {
        List<ViolationDTO> violations = ex.getConstraintViolations().stream()
                .map(
                        violation -> new ViolationDTO(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .toList();

        return new ErrorDTO(
                LocalDateTime.now(),
                "Arguments of request (path or query) isn't valid. See violation list.",
                "Validation failed",
                violations
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorDTO handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<ViolationDTO> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ViolationDTO(error.getField(), error.getDefaultMessage()))
                .toList();
        return new ErrorDTO(
                LocalDateTime.now(),
                "Body of request isn't valid. See violation list.",
                "Validation failed",
                violations
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorDTO handleConversionFail(HttpMessageNotReadableException e) {

        return new ErrorDTO(
                LocalDateTime.now(),
                "Conversion failed",
                e.getMessage(),
                null
        );
    }

}
