package kitchenpos.menuproduct.event;

import kitchenpos.menuproduct.domain.MenuProductValidator;
import kitchenpos.menu.event.MenuCreatedEvent;
import kitchenpos.menuproduct.domain.MenuProduct;
import kitchenpos.menuproduct.domain.MenuProductRepository;
import kitchenpos.menuproduct.domain.MenuProducts;
import kitchenpos.menuproduct.dto.MenuProductRequest;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuProductEventHandler {

    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuProductEventHandler(ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Async
    @EventListener
    public void saveMenuProduct(MenuCreatedEvent event) {
        List<MenuProduct> menuProductList = new ArrayList<>();

        for (MenuProductRequest menuProductRequest : event.getMenuProductRequests()) {
            productRepository.findById(menuProductRequest.getProductId()).ifPresent(
                    product -> menuProductList.add(
                            new MenuProduct(event.getMenu(), product, menuProductRequest.getQuantity())
                    )
            );
        }

        MenuProducts menuProducts = new MenuProducts(menuProductList);

        MenuProductValidator.validatePrice(menuProducts, event.getMenu());

        menuProductRepository.saveAll(menuProducts.menuProducts());
    }
}