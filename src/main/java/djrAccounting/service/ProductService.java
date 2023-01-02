package djrAccounting.service;

import djrAccounting.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto findById(Long id);

    List<ProductDto> getAllProducts();

    void deleteProductById(Long id);

    boolean productExistByCategory(Long categoryId);

    void update(ProductDto productDto);

    void save(ProductDto productDto);
}
