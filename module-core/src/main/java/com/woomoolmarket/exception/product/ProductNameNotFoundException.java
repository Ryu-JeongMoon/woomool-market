package com.woomoolmarket.exception.product;

public class ProductNameNotFoundException extends RuntimeException {

    public ProductNameNotFoundException(String message) {
        super(message);
    }
}
