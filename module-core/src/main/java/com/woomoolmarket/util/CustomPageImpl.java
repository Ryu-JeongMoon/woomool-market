package com.woomoolmarket.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class CustomPageImpl<T> extends PageImpl<T> {

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public CustomPageImpl(
    @JsonProperty("content") List<T> content,
    @JsonProperty("page") int page,
    @JsonProperty("size") int size,
    @JsonProperty("totalElements") long total) {

    super(content, PageRequest.of(page, size), total);
  }

  @Override
  @JsonIgnore
  public Pageable getPageable() {
    return super.getPageable();
  }
}
