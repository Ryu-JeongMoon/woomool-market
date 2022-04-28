package com.woomoolmarket.util.errors;


import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.validation.BindingResult;

public class JsonBindingResultModule extends SimpleModule {

  public JsonBindingResultModule() {
    addSerializer(BindingResult.class, new BindingResultSerializer());
  }
}
