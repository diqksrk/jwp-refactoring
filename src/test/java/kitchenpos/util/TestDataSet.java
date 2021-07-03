package kitchenpos.util;

import java.math.BigDecimal;
import java.util.Arrays;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class TestDataSet {
    public static final MenuGroup 추천_메뉴_그륩 = new MenuGroup(1L, "추천메뉴");
    public static final MenuGroup 계절_메뉴_그룹 = new MenuGroup(2L, "계절메뉴");
    public static final Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
    public static final Product 양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(15000));
    public static final Product 후라이드 = new Product(3L, "후라이드", BigDecimal.valueOf(10000));
    public static final MenuProduct 후라이드_2개 = new MenuProduct(1L, 후라이드.getId(), 2);
    public static final MenuProduct 양념_2개 = new MenuProduct(2L, 양념치킨.getId(), 2);
    public static final OrderTable 테이블_1번 = new OrderTable(1L, 4, true);
    public static final OrderTable 테이블_2번 = new OrderTable(2L, 2, true);
    public static final OrderTable 테이블_3번_존재 = new OrderTable(3L, 2, false);
    public static final OrderTable 테이블_4번_존재 = new OrderTable(4L, 4, false);
    public static final TableGroup 산악회 = new TableGroup(1L, Arrays.asList(테이블_1번, 테이블_2번));

    public static final Menu 원플원_후라이드 = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), 추천_메뉴_그륩.getId(),
        Arrays.asList(후라이드_2개));

    public static final Menu 원플원_양념 = new Menu(2L, "양념+양념", BigDecimal.valueOf(19000), 추천_메뉴_그륩.getId(),
        Arrays.asList(양념_2개));

    public static final Order 주문_1번 = new Order(1L, 테이블_3번_존재.getId(),
        Arrays.asList(new OrderLineItem(원플원_후라이드.getId(), 4), new OrderLineItem(원플원_양념.getId(), 10)));

    public static final Order 주문_2번 = new Order(2L, 테이블_4번_존재.getId(),
        Arrays.asList(new OrderLineItem(원플원_후라이드.getId(), 1), new OrderLineItem(원플원_양념.getId(), 2)));

}