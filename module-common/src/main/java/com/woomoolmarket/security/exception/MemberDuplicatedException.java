package com.woomoolmarket.security.exception;

public class MemberDuplicatedException extends RuntimeException {

    public MemberDuplicatedException() {
        super();
    }

    public MemberDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberDuplicatedException(String message) {
        super(message);
    }

    public MemberDuplicatedException(Throwable cause) {
        super(cause);
    }
}
