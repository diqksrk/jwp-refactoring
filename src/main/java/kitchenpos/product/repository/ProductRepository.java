package kitchenpos.product.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}