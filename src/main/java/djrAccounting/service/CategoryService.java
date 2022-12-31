package djrAccounting.service;

import djrAccounting.dto.CategoryDto;


import java.util.List;

public interface CategoryService {

    CategoryDto findById(Long id);

    List<CategoryDto> listAllCategories();

    void save(CategoryDto categoryDto);
}
