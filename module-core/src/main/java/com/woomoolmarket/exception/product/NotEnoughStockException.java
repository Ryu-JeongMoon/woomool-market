package com.woomoolmarket.exception.product;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(String message) {
        super(message);
    }
}
