package us.vicentini.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final String EXCEPTION_ATTRIBUTE_NAME = "exception";


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public String handleNumberFormatException(NumberFormatException ex, Model model) {
        log.error("Handling Number Format Exception: {}", ex.getMessage());
        model.addAttribute(EXCEPTION_ATTRIBUTE_NAME, ex);
        return "400error";
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public String handleWebExchangeBindException(WebExchangeBindException ex, Model model) {
        log.error("Handling Web Exchange Bind Exception: {}", ex.getMessage());
        model.addAttribute(EXCEPTION_ATTRIBUTE_NAME, ex);
        return "400error";
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Model model) {
        log.error("Handling not found exception: {}", ex.getMessage());
        model.addAttribute(EXCEPTION_ATTRIBUTE_NAME, ex);
        return "404error";
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public String handleWebExchangeBindException(Throwable ex, Model model) {
        log.error("Handling Exceptions: {}", ex.getMessage(), ex);
        model.addAttribute(EXCEPTION_ATTRIBUTE_NAME, ex);
        return "500error";
    }
}
