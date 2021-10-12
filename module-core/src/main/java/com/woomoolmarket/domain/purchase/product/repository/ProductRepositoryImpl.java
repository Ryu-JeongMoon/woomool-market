package com.woomoolmarket.domain.purchase.product.repository;

import static com.woomoolmarket.domain.member.entity.QMember.member;
import static com.woomoolmarket.domain.purchase.product.entity.QProduct.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.querydsl.QueryDslUtil;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findByPriceRange(Integer minPrice, Integer maxPrice) {
        return queryFactory
            .selectFrom(product)
            .where(searchByPriceRange(minPrice, maxPrice))
            .fetch();
    }

    @Override
    public List<Product> findByCondition(ProductSearchCondition searchCondition) {
        return queryFactory.selectFrom(product)
            .leftJoin(product.member, member)
            .fetchJoin()
            .where(searchedByAll(searchCondition))
            .fetch();
    }

    @Override
    public List<Product> findByConditionForAdmin(ProductSearchCondition searchCondition) {
        return queryFactory.selectFrom(product)
            .leftJoin(product.member, member)
            .fetchJoin()
            .where(searchedByAllForAdmin(searchCondition))
            .fetch();
    }

    private BooleanBuilder priceLoe(int maxPrice) {
        return QueryDslUtil.nullSafeBuilder(() -> product.price.loe(maxPrice));
    }

    private BooleanBuilder priceGoe(int minPrice) {
        return QueryDslUtil.nullSafeBuilder(() -> product.price.goe(minPrice));
    }

    private BooleanBuilder searchByPriceRange(Integer minPrice, Integer maxPrice) {
        return priceGoe(minPrice)
            .and(priceLoe(maxPrice));
    }

    private BooleanBuilder nameContains(String name) {
        return QueryDslUtil.nullSafeBuilder(() -> product.name.contains(name));
    }

    private BooleanBuilder emailContains(String email) {
        return QueryDslUtil.nullSafeBuilder(() -> product.member.email.contains(email));
    }

    private BooleanBuilder regionEq(Region region) {
        return QueryDslUtil.nullSafeBuilder(() -> product.region.eq(region));
    }

    private BooleanBuilder statusEq(Status status) {
        return QueryDslUtil.nullSafeBuilder(() -> product.status.eq(status));
    }

    private BooleanBuilder categoryEq(ProductCategory category) {
        return QueryDslUtil.nullSafeBuilder(() -> product.productCategory.eq(category));
    }

    private BooleanBuilder searchedByAll(ProductSearchCondition searchCondition) {
        return nameContains(searchCondition.getName())
            .and(emailContains(searchCondition.getEmail()))
            .and(regionEq(searchCondition.getRegion()))
            .and(statusEq(Status.ACTIVE))
            .and(categoryEq(searchCondition.getCategory()));
    }

    private BooleanBuilder searchedByAllForAdmin(ProductSearchCondition searchCondition) {
        return nameContains(searchCondition.getName())
            .and(emailContains(searchCondition.getEmail()))
            .and(regionEq(searchCondition.getRegion()))
            .and(statusEq(searchCondition.getStatus()))
            .and(categoryEq(searchCondition.getCategory()));
    }
}

/*
TODO eq -> contains 검색 조건 변경 필요
메서드 많아서 지저분해 보인당
해결 방법이 있을 것인가?!
 */