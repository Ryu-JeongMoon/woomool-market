package com.woomoolmarket.exception.member;

public class UsernameDuplicatedException extends RuntimeException {

    public UsernameDuplicatedException(String message) {
        super(message);
    }
}
