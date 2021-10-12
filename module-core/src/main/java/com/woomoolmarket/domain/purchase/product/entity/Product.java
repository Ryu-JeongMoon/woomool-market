package com.woomoolmarket.domain.purchase.product.entity;

import com.woomoolmarket.common.auditing.BaseEntity;
import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.member.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    private String name;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Member member;

    private String description;
    private String productImage;

    private Integer price;
    private int stock;

    private LocalDateTime deletedDateTime;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Builder
    public Product(Member member, String name, Integer price, int stock, String description, String productImg,
        ProductCategory productCategory, Region region) {
        this.member = member;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.productImage = productImg;
        this.productCategory = productCategory;
        this.region = region;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException(ExceptionUtil.NOT_ENOUGH_STOCK);
        }
        this.stock -= quantity;
    }

    public void delete() {
        changeStatusAndLeaveDateTime(Status.INACTIVE, LocalDateTime.now());
    }

    public void restore() {
        changeStatusAndLeaveDateTime(Status.ACTIVE, null);
    }

    public void changeStatusAndLeaveDateTime(Status memberStatus, LocalDateTime deletedDateTime) {
        this.status = memberStatus;
        this.deletedDateTime = deletedDateTime;
    }
}