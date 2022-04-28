package com.woomoolmarket.util.wrapper;

@FunctionalInterface
public interface ConsumerWithException<T, E extends Exception> {

  void accept(T t) throws E;
}
