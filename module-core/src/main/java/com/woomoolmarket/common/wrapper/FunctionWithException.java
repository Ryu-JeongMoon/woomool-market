package com.woomoolmarket.common.wrapper;

@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {

  R apply(T t) throws E;
}
