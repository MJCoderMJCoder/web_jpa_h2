package com.spring_boot.web_jpa_h2.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("该订单不存在");
    }
}
