package com.woomoolmarket.service.order.dto.request;

import java.util.Collection;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderRequest {

  @NotNull
  @Min(value = 1L)
  private Long memberId;

  @NotEmpty
  private Collection<Long> cartIds;
}
