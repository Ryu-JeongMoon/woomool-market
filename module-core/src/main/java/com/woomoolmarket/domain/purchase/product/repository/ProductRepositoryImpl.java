package com.woomoolmarket.domain.purchase.product.repository;

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
            .where(priceRange(minPrice, maxPrice))
            .fetch();
    }

    @Override
    public List<Product> findByCondition(ProductSearchCondition searchCondition) {
        return queryFactory.selectFrom(product)
            .where(searchedByAll(searchCondition))
            .fetch();
    }

    private BooleanBuilder priceLoe(int maxPrice) {
        return QueryDslUtil.nullSafeBuilder(() -> product.price.loe(maxPrice));
    }

    private BooleanBuilder priceGoe(int minPrice) {
        return QueryDslUtil.nullSafeBuilder(() -> product.price.goe(minPrice));
    }

    private BooleanBuilder priceRange(Integer minPrice, Integer maxPrice) {
        return priceGoe(minPrice).and(priceLoe(maxPrice));
    }

    private BooleanBuilder nameEq(String name) {
        return QueryDslUtil.nullSafeBuilder(() -> product.name.eq(name));
    }

    private BooleanBuilder sellerEq(String seller) {
        return QueryDslUtil.nullSafeBuilder(() -> product.seller.eq(seller));
    }

    private BooleanBuilder statusEq(Status status) {
        return QueryDslUtil.nullSafeBuilder(() -> product.status.eq(status));
    }

    private BooleanBuilder categoryEq(ProductCategory category) {
        return QueryDslUtil.nullSafeBuilder(() -> product.productCategory.eq(category));
    }

    private BooleanBuilder regionEq(Region region) {
        return QueryDslUtil.nullSafeBuilder(() -> product.region.eq(region));
    }

    private BooleanBuilder searchedByAll(ProductSearchCondition searchCondition) {
        return nameEq(searchCondition.getName())
            .and(sellerEq(searchCondition.getSeller()))
            .and(categoryEq(searchCondition.getCategory()))
            .and(regionEq(searchCondition.getRegion()))
            .and(statusEq(searchCondition.getStatus()));
    }
}

/*
TODO eq -> contains 검색 조건 변경 필요
메서드 많아서 지저분해 보인당
해결 방법이 있을 것인가?!
 */