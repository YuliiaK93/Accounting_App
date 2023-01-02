package djrAccounting.repository;

import djrAccounting.entity.Category;
import djrAccounting.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategory_Id(Long id);


}
