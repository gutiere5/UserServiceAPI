package com.noel.controller;

import com.amazonaws.services.cognitoidp.model.UnauthorizedException;
import com.noel.model.ErrorMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

public class BaseController {
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String NOT_FOUND = "Not Found";

    @ExceptionHandler(UnauthorizedException.class)
    public ErrorMessage catchUnauthorized(UnauthorizedException e) {
        return new ErrorMessage(401, UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorMessage noElement(NoSuchElementException e) {
        return new ErrorMessage(404, NOT_FOUND);
    }
}
