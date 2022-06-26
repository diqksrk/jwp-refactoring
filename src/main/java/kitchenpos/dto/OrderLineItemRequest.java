package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private int quantity;

    protected OrderLineItemRequest() {
    }

    private OrderLineItemRequest(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, int quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderLineItem toOrderLineItem(Long orderId) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
