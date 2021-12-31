package kitchenpos.common.fixtrue;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    private ProductFixture() {

    }

    public static Product of(String name, BigDecimal price) {
        return Product.of(name, price);
    }
}