package com.woomoolmarket.entity.funding.entity;

import com.woomoolmarket.common.BaseTimeEntity;
import com.woomoolmarket.common.Region;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EqualsAndHashCode(of = "funding_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Funding extends BaseTimeEntity {

    @Id
    @Column(name = "funding_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String image;

    private LocalDateTime deadline;

    private int price;

    private int targetMoney;

    private int currentMoney;

    @Enumerated(EnumType.STRING)
    private FundingStatus status;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Builder
    public Funding(String name, String description, String image, LocalDateTime deadline, int price, int targetMoney, int currentMoney, FundingStatus status, Region region) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.deadline = deadline;
        this.price = price;
        this.targetMoney = targetMoney;
        this.currentMoney = currentMoney;
        this.status = status;
        this.region = region;
    }
}
