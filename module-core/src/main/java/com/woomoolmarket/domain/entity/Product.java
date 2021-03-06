package com.woomoolmarket.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woomoolmarket.domain.entity.auditing.BaseEntity;
import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.util.AtomicIntegerConverter;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id", "name" }, callSuper = false)
@Table(uniqueConstraints = { @UniqueConstraint(name = "unique_product_name", columnNames = "name") })
public class Product extends BaseEntity {

  @Id
  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(max = 255)
  @Column(nullable = false)
  private String name;

  @JsonIgnore
  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Image> images = new ArrayList<>();

  @Lob
  @Column(nullable = false)
  private String description;

  @Size(max = 255)
  private String productImage;

  @Min(1000L)
  @Column(nullable = false, columnDefinition = "integer default 1000")
  private Integer price;

  @Min(100L)
  @Convert(converter = AtomicIntegerConverter.class)
  @Column(nullable = false, columnDefinition = "integer default 100")
  private AtomicInteger stock;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "product_count_id")
  private ProductCount productCount;

  private LocalDateTime deletedDateTime;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status = Status.ACTIVE;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductCategory productCategory;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Region region;

  @Builder
  public Product(Member member, String name, Integer price, Integer stock, String description, String productImage,
    ProductCategory productCategory, Region region) {
    this.member = member;
    this.name = name;
    this.price = price;
    this.stock = new AtomicInteger(stock);
    this.description = description;
    this.productImage = productImage;
    this.productCategory = productCategory;
    this.region = region;
  }

  public void increaseStock(Integer quantity) {
    this.stock.addAndGet(quantity);
  }

  public void decreaseStock(int quantity) {
    if (this.stock.intValue() < quantity) {
      throw new IllegalArgumentException(ExceptionMessages.Product.NOT_ENOUGH_STOCK);
    }
    this.stock.getAndUpdate(stock -> stock - quantity);
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

  public void addImages(List<Image> images) {
    if (CollectionUtils.isEmpty(images)) {
      return;
    }
    this.images = images;
    images.forEach(image -> image.setProduct(this));
  }
}