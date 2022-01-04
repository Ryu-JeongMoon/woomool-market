package com.woomoolmarket.common.wrapper;

@FunctionalInterface
public interface ConsumerWithException<T, E extends Exception> {

  void accept(T t) throws E;
}
