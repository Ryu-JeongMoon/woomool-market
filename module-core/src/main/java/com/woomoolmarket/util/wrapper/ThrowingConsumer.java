package com.woomoolmarket.util.wrapper;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {

  static <T, E extends Throwable> Consumer<T> unchecked(ThrowingConsumer<T, E> f) {
    return t -> {
      try {
        f.accept(t);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    };
  }

  static <T, E extends Throwable> Consumer<T> unchecked(ThrowingConsumer<T, E> f, Consumer<Throwable> c) {
    return t -> {
      try {
        f.accept(t);
      } catch (Throwable e) {
        c.accept(e);
      }
    };
  }

  void accept(T t) throws E;
}
