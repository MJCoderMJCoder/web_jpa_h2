package com.spring_boot.web_jpa_h2.controller;

import com.spring_boot.web_jpa_h2.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * When an EmployeeNotFoundException is thrown, this extra tidbit of Spring MVC configuration is used to render an HTTP 404:
 */
@ControllerAdvice
public class EmployeeNotFoundAdvice {

    /**
     * @param employeeNotFoundException
     * @return
     * @ResponseBody signals that this advice is rendered straight into the response body.
     * @ExceptionHandler configures the advice to only respond if an EmployeeNotFoundException is thrown.
     * @ResponseStatus says to issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String employeeNotFoundHandler(EmployeeNotFoundException employeeNotFoundException) {
        return employeeNotFoundException.getMessage();
    }
}
