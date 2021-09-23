package com.woomoolmarket.common.embeddable;

import com.woomoolmarket.common.auditing.BaseTimeEntity;
import com.woomoolmarket.domain.member.entity.Address;
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
