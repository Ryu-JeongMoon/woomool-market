package com.woomoolmarket.domain.count.entity;

import com.woomoolmarket.domain.purchase.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_count")
public class ProductCount {

  @Id
  @Column(name = "product_count_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int hitCount;

  private int likeCount;

  @OneToOne(mappedBy = "productCount", orphanRemoval = true)
  private Product product;
}
