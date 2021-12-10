package com.woomoolmarket.domain.purchase.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woomoolmarket.common.auditing.BaseEntity;
import com.woomoolmarket.common.constant.ExceptionConstant;
import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Product extends BaseEntity {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 24)
    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Lob
    @Column(nullable = false)
    private String description;

    @Size(max = 255)
    private String productImage;

    @Column(nullable = false, columnDefinition = "integer default 1000")
    private Integer price;

    @Column(nullable = false, columnDefinition = "integer default 100")
    private Integer stock;

    private LocalDateTime deletedDateTime;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Builder
    public Product(Member member, String name, Integer price, Integer stock, String description, String productImage,
        ProductCategory productCategory, Region region) {
        this.member = member;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.productImage = productImage;
        this.productCategory = productCategory;
        this.region = region;
    }

    public void increaseStock(Integer quantity) {
        this.stock += quantity;
    }

    public void decreaseStock(Integer quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException(ExceptionConstant.PRODUCT_NOT_ENOUGH_STOCK);
        }
        this.stock -= quantity;
    }

    public void delete() {
        changeStatusAndLeaveDateTime(Status.INACTIVE, LocalDateTime.now());
    }

    public void restore() {
        changeStatusAndLeaveDateTime(Status.ACTIVE, null);
    }

    private void changeStatusAndLeaveDateTime(Status memberStatus, LocalDateTime deletedDateTime) {
        this.status = memberStatus;
        this.deletedDateTime = deletedDateTime;
    }
}

// TODO, increase & decrease stock 동시성 문제 발생하지 않을까? 어떻게 해결해야 하남?