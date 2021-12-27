package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.ordertable.domain.domain.OrderTable;
import kitchenpos.ordertable.domain.repo.OrderTableRepository;
import kitchenpos.ordertable.exception.NotFoundOrderTableException;
import kitchenpos.tablegroup.domain.domain.TableGroup;
import kitchenpos.tablegroup.domain.service.TableGroupExternalValidator;
import kitchenpos.tablegroup.domain.repo.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupAddRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@InjectMocks
	private TableGroupService tableGroupService;

	@Mock
	private TableGroupExternalValidator tableGroupExternalValidator;
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private TableGroupRepository tableGroupRepository;

	@DisplayName("그룹 생성")
	@Test
	void create() {
		final List<OrderTable> 주문테이블_목록 = Arrays.asList(
			OrderTable.of(1L, null, 4, true),
			OrderTable.of(2L, null, 6, true)
		);
		final TableGroup 그룹 = TableGroup.of(1L, Arrays.asList(
			OrderTable.of(1L, null, 4, true),
			OrderTable.of(2L, null, 6, true)
		));

		given(orderTableRepository.findAllById(any())).willReturn(주문테이블_목록);
		given(tableGroupRepository.save(any())).willReturn(그룹);

		final List<Long> 주문테이블_ID목록 = 주문테이블_목록.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());
		final TableGroupResponse createdTableGroup = tableGroupService.create(
			TableGroupAddRequest.of(주문테이블_ID목록)
		);

		assertAll(
			() -> assertThat(createdTableGroup.getId()).isNotNull(),
			() -> assertThat(createdTableGroup.getOrderTables().size()).isEqualTo(2)
		);
	}

	@DisplayName("그룹 생성: 주문 테이블이 존재하지 않으면 예외발생")
	@Test
	void create_not_found_order_table() {
		final OrderTable 주문테이블1 = OrderTable.of(1L, null, 3, true);
		given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(주문테이블1));

		assertThatExceptionOfType(NotFoundOrderTableException.class)
			.isThrownBy(() -> tableGroupService.create(
				TableGroupAddRequest.of(Arrays.asList(주문테이블1.getId(), 2L))
			));
	}

	@DisplayName("그룹 해지")
	@Test
	void ungroup() {
		final OrderTable 주문테이블1 = OrderTable.of(1L, null, 3, true);
		final OrderTable 주문테이블2 = OrderTable.of(2L, null, 2, true);
		final TableGroup 그룹 = TableGroup.of(1L, Arrays.asList(주문테이블1, 주문테이블2));

		given(tableGroupRepository.findById(any())).willReturn(Optional.of(그룹));

		tableGroupService.ungroup(그룹.getId());

		assertAll(
			() -> assertThat(주문테이블1.getTableGroupId()).isNull(),
			() -> assertThat(주문테이블2.getTableGroupId()).isNull()
		);
	}
}