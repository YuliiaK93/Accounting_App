package djrAccounting.service;

import djrAccounting.dto.ProductDto;

public interface ProductService {

    ProductDto findById( Long id);
}
