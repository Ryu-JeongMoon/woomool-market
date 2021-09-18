package com.woomoolmarket.controller.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {

    private T content;

    protected Result() {
        this.content = null;
    }

    public static <T> Result<T> of(T content) {
        return new Result<>(content);
    }
}
