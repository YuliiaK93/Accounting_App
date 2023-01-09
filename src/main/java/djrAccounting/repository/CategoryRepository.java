package djrAccounting.repository;

import djrAccounting.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCompany_IdOrderByDescriptionAsc(Long id);

    boolean existsByDescriptionAndCompany_Id(String description, Long companyId);
}
