package com.woomoolmarket.service.cart.dto.request;

import java.io.Serializable;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartIdRequest implements Serializable {

  private Collection<Long> cartIds;
}
