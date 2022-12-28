package djrAccounting.service;

import djrAccounting.dto.CategoryDto;
import djrAccounting.dto.ProductDto;

public interface CategoryService {

    CategoryDto findById(Long id);
}
