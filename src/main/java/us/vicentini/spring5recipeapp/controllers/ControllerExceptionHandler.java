package us.vicentini.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(NumberFormatException.class)
//    public ModelAndView handleNumberFormatException(NumberFormatException ex) {
//        log.error("Handling Number Format Exception: {}", ex.getMessage());
//        ModelAndView modelAndView = new ModelAndView("400error");
//        modelAndView.addObject("exception", ex);
//        return modelAndView;
//    }
//
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NotFoundException.class)
//    public ModelAndView handleNotFoundException(NotFoundException ex) {
//        log.error("Handling not found exception: {}", ex.getMessage());
//        ModelAndView modelAndView = new ModelAndView("404error");
//        modelAndView.addObject("exception", ex);
//        return modelAndView;
//    }
}
