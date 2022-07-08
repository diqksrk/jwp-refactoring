package kitchenpos.table.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean enabledTableClear() {
        return this.equals(COMPLETION);
    }
}
