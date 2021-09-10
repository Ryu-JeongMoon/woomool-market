package com.woomoolmarket.model.purchase.cart.entity;

import com.woomoolmarket.model.member.entity.Address;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    private String receiver;

    @Embedded
    private Address address;

    private String phone;

}
