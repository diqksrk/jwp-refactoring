package kitchenpos.table.domain;

import kitchenpos.order.domain.*;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.exception.UnableChangeEmptyOrderTableException;
import kitchenpos.table.exception.UnableChangeNumberOfGuestsException;
import kitchenpos.table.exception.UnableOrderCausedByEmptyTableException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGeusts numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGeusts.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGeusts.of(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void groupBy(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(NumberOfGeusts numberOfGuests) {
        if (isEmpty()) {
            throw new UnableChangeNumberOfGuestsException("빈테이블은 손님의 수를 변경할수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(OrderTableRequest orderTableRequest) {
        if (Objects.nonNull(getTableGroupId())) {
            throw new UnableChangeEmptyOrderTableException("단체테이블인 경우 테이블을 비울수 없습니다.");
        }

        this.empty = orderTableRequest.isEmpty();
    }

    public boolean isUnableTableGroup() {
        if (isEmpty() || Objects.nonNull(getTableGroupId())) {
            return true;
        }
        return false;
    }

    public void ungroup() {
        groupBy(null);
    }

    public Order newOrder(LocalDateTime orderedTime, List<OrderLineItem> newOrderLineItems) {
        if (isEmpty()) {
            throw new UnableOrderCausedByEmptyTableException("빈테이블은 주문을 할수 없습니다.");
        }
        Order newOrder = Order.newOrder(this, orderedTime, newOrderLineItems);
        newOrder.reception();
        return newOrder;
    }
}
