package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    @DisplayName("주문 목록은 한개 이상 존재해야 합니다.")
    public void validSize(){
        //given
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> OrderLineItems.from(orderLineItems)
        );
    }

    @Test
    @DisplayName("주문 목록은 존재해야 합니다.")
    public void validNull(){
        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> OrderLineItems.from(null)
        );
    }

    @Test
    @DisplayName("주문 목록의 주문을 변경한다.")
    public void changeOrder() {
        //given
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now()
                , OrderLineItems.from(Collections.singletonList(OrderLineItem.of(1L, BigDecimal.ONE, "에이스", 2))));

        final List<OrderLineItem> orderLineItemList = Arrays.asList(OrderLineItem.of(1L, BigDecimal.ONE, "에이스", 2),
                OrderLineItem.of(1L, BigDecimal.ONE, "에이스", 2));

        //given
        OrderLineItems orderLineItems = OrderLineItems.from(orderLineItemList);

        //when
        orderLineItems.changeOrder(order);
        //then
        assertThat(orderLineItems.value()).extracting("order.id").hasSize(2);
    }

}