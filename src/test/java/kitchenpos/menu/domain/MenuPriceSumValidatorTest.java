package kitchenpos.menu.domain;

import kitchenpos.menu.domain.validator.MenuPriceSumValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@DisplayName("메뉴 가격 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class MenuPriceSumValidatorTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private MenuPriceSumValidator menuPriceValidator;

    @DisplayName("메뉴의 가격은 포함된 상품들의 금액의 합 보다 크지 않을경우 유효하다.")
    @Test
    void validate() {
        // given
        final MenuProductGroup menuProductGroup = MenuProductGroup.ofRequests(Arrays.asList(
                new MenuProductRequest(1L, 2),
                new MenuProductRequest(2L, 3)
        ));

        final MenuPrice price = MenuPrice.of(6000);

        given(productService.getProduct(1L)).willReturn(Product.generate(1L, "마일드참치캔", 1000));
        given(productService.getProduct(2L)).willReturn(Product.generate(2L, "고추참치캔", 2000));

        // when
        final Executable executable = () -> menuPriceValidator.validate(price, menuProductGroup);
        // then
        assertDoesNotThrow(executable);
    }

    @DisplayName("메뉴의 가격은 포함된 상품들의 금액의 합 보다 클 경우 유효하지 못하다.")
    @Test
    void validateByIllegalPrice() {
        // given
        final MenuProductGroup menuProductGroup = MenuProductGroup.ofRequests(Arrays.asList(
                new MenuProductRequest(1L, 2),
                new MenuProductRequest(2L, 3)
        ));

        final MenuPrice price = MenuPrice.of(9000);

        given(productService.getProduct(1L)).willReturn(Product.generate(1L, "마일드참치캔", 1000));
        given(productService.getProduct(2L)).willReturn(Product.generate(2L, "고추참치캔", 2000));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> menuPriceValidator.validate(price, menuProductGroup);
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품그룹의 메뉴 아이디에 따른 메뉴 상품의 상품이 존재하지 않을 경우")
    @Test
    void createByNotExistProduct() {
        // given
        final MenuProductGroup menuProductGroup = MenuProductGroup.ofRequests(Arrays.asList(
                new MenuProductRequest(1L, 2),
                new MenuProductRequest(2L, 3)
        ));

        final MenuPrice price = MenuPrice.of(9000);

        doThrow(IllegalArgumentException.class).when(productService).getProduct(anyLong());

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> menuPriceValidator.validate(price, menuProductGroup);
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
    }

}