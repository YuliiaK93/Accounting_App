package djrAccounting.service;

import djrAccounting.dto.CategoryDto;


import java.util.List;

public interface CategoryService {
    CategoryDto findById(Long id);

    List<CategoryDto> listAllCategories();

    CategoryDto save(CategoryDto categoryDto);

    boolean isCategoryDescriptionExist(String description);
    void deleteCategoryById(Long id);

    CategoryDto update(CategoryDto category);
}
