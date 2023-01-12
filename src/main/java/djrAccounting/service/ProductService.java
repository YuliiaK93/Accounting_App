package djrAccounting.service;

import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto findById(Long id);

    List<ProductDto> getAllProducts();

    void deleteProductById(Long id);

    boolean productExistByCategory(Long categoryId);

    List<ProductDto> listProductsBySelectedUserCompany();

    boolean isStockEnough(InvoiceProductDto invoiceProductDto);

    ProductDto update(ProductDto productDto);

    ProductDto save(ProductDto productDto);

    void decreaseQuantityInStock(Long productId, int quantity);

    boolean isNameNotPrevious(Long id, String name);

    boolean isNameAlreadyInUse(String name);
}
