package djrAccounting.repository;

import djrAccounting.entity.Category;
import djrAccounting.entity.Company;
import djrAccounting.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategory_Id(Long id);
    List<Product> findAllByCategoryCompany(Company company);

    List<Product> findAllByCategoryCompany(Company company);

    Product findByName(String name);




}
