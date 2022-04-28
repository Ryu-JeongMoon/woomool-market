package com.woomoolmarket.util;

import com.woomoolmarket.util.wrapper.ConsumerWithException;
import com.woomoolmarket.util.wrapper.FunctionWithException;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FunctionalWrapperUtils {

  public static <T, R, E extends Exception> Function<T, R> wrapFunction(FunctionWithException<T, R, E> fe) {
    return arg -> {
      try {
        return fe.apply(arg);
      } catch (Exception e) {
        log.info("[WOOMOOL-ERROR] :: Wrapping Failed => {}", e.getMessage());
        throw new RuntimeException(e);
      }
    };
  }

  public static <T, E extends Exception> Consumer<T> wrapConsumer(ConsumerWithException<T, E> ce) {
    return arg -> {
      try {
        ce.accept(arg);
      } catch (Exception e) {
        log.info("[WOOMOOL-ERROR] :: Wrapping Failed => {}", e.getMessage());
        throw new RuntimeException(e);
      }
    };
  }
}
