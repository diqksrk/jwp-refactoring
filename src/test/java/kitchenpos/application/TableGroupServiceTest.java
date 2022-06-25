package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {
    
    @Autowired
    ServiceTestHelper serviceTestHelper;

    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    void 테이블그룹_지정() {
        int numberOfTables = 2;
        TableGroup savedTableGroup = serviceTestHelper.테이블그룹_지정됨(numberOfTables);

        List<OrderTable> orderTables = savedTableGroup.getOrderTables();
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(orderTables).hasSize(numberOfTables);
        orderTables.stream()
                .forEach(table -> assertThat(table.getTableGroupId()).isEqualTo(savedTableGroup.getId()));
    }

    @Test
    void 테이블그룹_지정_저장되지않은_테이블로_그룹지정을_시도하는경우() {
        OrderTable newOrderTable = OrderTableFixtureFactory.createEmptyOrderTable();
        OrderTable newOrderTable2 = OrderTableFixtureFactory.createEmptyOrderTable();

        assertThatIllegalArgumentException().isThrownBy(()->{
            serviceTestHelper.테이블그룹_지정됨(newOrderTable,newOrderTable2);
        });
    }

    @Test
    void 테이블그룹_지정_테이블이_2개미만인경우() {
        int numberOfTables = 1;
        assertThatIllegalArgumentException().isThrownBy(()->{
            serviceTestHelper.테이블그룹_지정됨(numberOfTables);
        });
    }

    @Test
    void 테이블그룹_지정_비어있지않은_테이블이_포함된_경우() {
        OrderTable emptyTable = serviceTestHelper.빈테이블_생성됨();
        OrderTable notEmptyTable = serviceTestHelper.비어있지않은테이블_생성됨(3);

        assertThatIllegalArgumentException().isThrownBy(()->{
            serviceTestHelper.테이블그룹_지정됨(emptyTable,notEmptyTable);
        });
    }

    @Test
    void 테이블그룹_지정_다른_테이블그룹에_포함된_테이블이_있는_경우() {
        OrderTable emptyTable1 = serviceTestHelper.빈테이블_생성됨();
        OrderTable emptyTable2 = serviceTestHelper.빈테이블_생성됨();
        OrderTable emptyTable3 = serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.테이블그룹_지정됨(emptyTable1,emptyTable2);

        assertThatIllegalArgumentException().isThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(emptyTable2,emptyTable3));
    }

    @Test
    void 테이블그룹_지정해제_빈테이블인_경우() {
        OrderTable table1 = serviceTestHelper.빈테이블_생성됨();
        OrderTable table2 = serviceTestHelper.빈테이블_생성됨();
        TableGroup tableGroup = serviceTestHelper.테이블그룹_지정됨(table1,table2);

        tableGroupService.ungroup(tableGroup.getId());

        테이블그룹_해제여부확인(Lists.newArrayList(table1,table2));
    }



    private void 테이블그룹_해제여부확인(List<OrderTable> ungroupedTables){
        List<OrderTable> tables = tableService.list();
        ungroupedTables.stream().forEach(orderTable -> {
            OrderTable foundTable = findOrderTableById(orderTable.getId(),tables);
            assertThat(foundTable.getTableGroupId()).isNull();
        });
    }

    private OrderTable findOrderTableById(Long tableId, List<OrderTable> tables){
        return tables.stream()
                .filter(orderTable -> tableId.equals(orderTable.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
