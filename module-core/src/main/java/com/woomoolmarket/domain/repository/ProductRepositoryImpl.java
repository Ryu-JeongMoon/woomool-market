package com.woomoolmarket.domain.repository;


import static com.woomoolmarket.domain.entity.QProduct.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.repository.querydto.ProductQueryResponse;
import com.woomoolmarket.domain.repository.querydto.ProductSearchCondition;
import com.woomoolmarket.domain.repository.querydto.QProductQueryResponse;
import com.woomoolmarket.util.CustomPageImpl;
import com.woomoolmarket.util.QueryDslUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private Page<ProductQueryResponse> findByTemplate(BooleanBuilder booleanBuilder, Pageable pageable) {
    QueryResults<ProductQueryResponse> results = queryFactory
      .select(new QProductQueryResponse(
        product.id, product.name, product.description, product.productImage, product.price, product.stock,
        product.status, product.region, product.createdDateTime, product.productCategory, product.member.email))
      .from(product)
      .leftJoin(product.member)
      .where(booleanBuilder)
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .orderBy(product.id.desc())
      .fetchResults();

    return new CustomPageImpl<>(results.getResults(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotal());
  }

  @Override
  public Page<ProductQueryResponse> searchBy(ProductSearchCondition condition, Pageable pageable) {
    return findByTemplate(combineBy(condition), pageable);
  }

  @Override
  public Page<ProductQueryResponse> searchByAdmin(ProductSearchCondition condition, Pageable pageable) {
    return findByTemplate(combineForAdminBy(condition), pageable);
  }

  private BooleanBuilder combineBy(ProductSearchCondition condition) {
    return nameContains(condition.getName())
      .and(emailContains(condition.getEmail()))
      .and(regionEquals(condition.getRegion()))
      .and(statusEquals(Status.ACTIVE))
      .and(categoryEquals(condition.getCategory()))
      .and(priceLoe(condition.getMaxPrice()))
      .and(priceGoe(condition.getMinPrice()));
  }

  private BooleanBuilder combineForAdminBy(ProductSearchCondition condition) {
    return nameContains(condition.getName())
      .and(emailContains(condition.getEmail()))
      .and(regionEquals(condition.getRegion()))
      .and(statusEquals(condition.getStatus()))
      .and(categoryEquals(condition.getCategory()))
      .and(priceLoe(condition.getMaxPrice()))
      .and(priceGoe(condition.getMinPrice()));
  }

  private BooleanBuilder priceLoe(Integer maxPrice) {
    return QueryDslUtils.nullSafeBuilder(() -> product.price.loe(maxPrice));
  }

  private BooleanBuilder priceGoe(Integer minPrice) {
    return QueryDslUtils.nullSafeBuilder(() -> product.price.goe(minPrice));
  }

  private BooleanBuilder nameContains(String name) {
    return QueryDslUtils.nullSafeBuilder(() -> product.name.contains(name));
  }

  private BooleanBuilder emailContains(String email) {
    return QueryDslUtils.nullSafeBuilder(() -> product.member.email.contains(email));
  }

  private BooleanBuilder regionEquals(Region region) {
    return QueryDslUtils.nullSafeBuilder(() -> product.region.eq(region));
  }

  private BooleanBuilder statusEquals(Status status) {
    return QueryDslUtils.nullSafeBuilder(() -> product.status.eq(status));
  }

  private BooleanBuilder categoryEquals(ProductCategory category) {
    return QueryDslUtils.nullSafeBuilder(() -> product.productCategory.eq(category));
  }
}