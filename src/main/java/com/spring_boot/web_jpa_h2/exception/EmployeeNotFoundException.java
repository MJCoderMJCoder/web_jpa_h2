package com.spring_boot.web_jpa_h2.exception;


public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("找不到该员工");
    }
}
