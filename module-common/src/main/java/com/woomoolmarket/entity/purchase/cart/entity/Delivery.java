package com.woomoolmarket.entity.purchase.cart.entity;

import com.woomoolmarket.entity.member.entity.Address;
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
