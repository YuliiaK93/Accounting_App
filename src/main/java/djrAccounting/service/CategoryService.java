package djrAccounting.service;

import djrAccounting.dto.CategoryDto;
import djrAccounting.entity.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto findById(Long id);

    List<CategoryDto> listAllCategories();
}
